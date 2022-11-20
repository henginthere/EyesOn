package com.d201.eyeson.view.blind.scanobstacle

import android.graphics.*
import android.media.Image
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.widget.ImageView
import com.d201.depth.depth.DepthTextureHandler
import com.d201.depth.depth.common.CameraPermissionHelper
import com.d201.depth.depth.common.DisplayRotationHelper
import com.d201.depth.depth.common.SnackbarHelper
import com.d201.depth.depth.common.TrackingStateHelper
import com.d201.depth.depth.rendering.BackgroundRenderer
import com.d201.depth.depth.rendering.ObjectRenderer
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentScanObstacleBinding
import com.d201.eyeson.util.*
import com.google.ar.core.*
import com.google.ar.core.exceptions.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.detector.ObjectDetector
import java.io.IOException
import java.util.*
import java.util.concurrent.Executor
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

private const val TAG = "ScanObstacleFragment"

@AndroidEntryPoint
class ScanObstacleFragment :
    BaseFragment<FragmentScanObstacleBinding>(R.layout.fragment_scan_obstacle),
    GLSurfaceView.Renderer, TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech

    private lateinit var surfaceView: GLSurfaceView

    private var installRequested = false
    private var isDepthSupported = false

    private var session: Session? = null
    private val messageSnackbarHelper: SnackbarHelper = SnackbarHelper()
    private var displayRotationHelper: DisplayRotationHelper? = null
    private lateinit var trackingStateHelper: TrackingStateHelper

    private lateinit var depthTexture: DepthTextureHandler
    private val backgroundRenderer: BackgroundRenderer = BackgroundRenderer()
    private val virtualObject: ObjectRenderer = ObjectRenderer()

    // Temporary matrix allocated here to reduce number of allocations for each frame.
    private val anchorMatrix = FloatArray(16)

    // Anchors created from taps used for object placing with a given color.
    private val OBJECT_COLOR = floatArrayOf(139.0f, 195.0f, 74.0f, 255.0f)
    private val anchors = ArrayList<Anchor>()

    private var showDepthMap = false

    private var lastSpeakTime = 0L

    private lateinit var detector: ObjectDetector
    private lateinit var gpuThread: Executor

    private lateinit var graphicOverlay: ImageView

    override fun init() {
        initView()
        gpuThread = newSingleThreadContext("plz").executor
        gpuThread.execute {
            val baseOptions = BaseOptions.builder().useGpu().build()

            // Step 2: Initialize the detector object
            val options = ObjectDetector.ObjectDetectorOptions.builder()
                .setMaxResults(MAX_RESULT)
                .setBaseOptions(baseOptions)
                .setScoreThreshold(SCORE_THRESHOLD)
                .build()
            detector = ObjectDetector.createFromFileAndOptions(
                requireContext(),
                "custom_models/${MODEL_FILE}",
                options
            )
        }
    }

    private fun initView() {
        CoroutineScope(newSingleThreadContext("plz")).launch(CoroutineName("plz")) {

        }
        displayRotationHelper = DisplayRotationHelper(requireContext())
        trackingStateHelper = TrackingStateHelper(requireActivity())
        depthTexture = DepthTextureHandler(requireContext())

        // Set up renderer.
        surfaceView = binding.surfaceview
        surfaceView.setPreserveEGLContextOnPause(true)
        surfaceView.setEGLContextClientVersion(2)
        surfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0) // Alpha used for plane blending.
        surfaceView.setRenderer(this)
        surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY)
        surfaceView.setWillNotDraw(false)

        installRequested = false

        tts = TextToSpeech(requireContext(), this)
        lastSpeakTime = System.currentTimeMillis()

        binding.apply {
            btnBack.apply {
                accessibilityDelegate = accessibilityEvent(this, requireContext())
                setOnClickListener { requireActivity().finish() }
            }
            this@ScanObstacleFragment.graphicOverlay = graphicOverlay
        }
    }

    override fun onResume() {
        super.onResume()
        if (session == null) {
            var exception: Exception? = null
            var message: String? = null
            try {
                when (ArCoreApk.getInstance()
                    .requestInstall(requireActivity(), !installRequested)) {
                    ArCoreApk.InstallStatus.INSTALL_REQUESTED -> {
                        installRequested = true
                        return
                    }
                    ArCoreApk.InstallStatus.INSTALLED -> {}
                }

                // ARCore requires camera permissions to operate. If we did not yet obtain runtime
                // permission on Android M and above, now is a good time to ask the user for it.
                if (!CameraPermissionHelper.hasCameraPermission(requireActivity())) {
                    CameraPermissionHelper.requestCameraPermission(requireActivity())
                    return
                }

                // Creates the ARCore session.
                session = Session( /* context= */requireContext())
                val config = session!!.config
                val filter = CameraConfigFilter(session)
                filter.targetFps = EnumSet.of(CameraConfig.TargetFps.TARGET_FPS_30) // 30프레임
                filter.depthSensorUsage.add(CameraConfig.DepthSensorUsage.REQUIRE_AND_USE) // tof 사용
//                filter.depthSensorUsage.add(CameraConfig.DepthSensorUsage.DO_NOT_USE) // tof 미사용
                val cameraConfigList = session!!.getSupportedCameraConfigs(filter)
                session!!.cameraConfig = cameraConfigList[1]
                isDepthSupported = session!!.isDepthModeSupported(Config.DepthMode.AUTOMATIC)

                if (isDepthSupported) {
                    config.setDepthMode(Config.DepthMode.AUTOMATIC)
                    config.setFocusMode(Config.FocusMode.AUTO)
                } else {
                    config.setDepthMode(Config.DepthMode.DISABLED)
                }
                session!!.configure(config)
            } catch (e: UnavailableArcoreNotInstalledException) {
                message = "Please install ARCore"
                exception = e
            } catch (e: UnavailableUserDeclinedInstallationException) {
                message = "Please install ARCore"
                exception = e
            } catch (e: UnavailableApkTooOldException) {
                message = "Please update ARCore"
                exception = e
            } catch (e: UnavailableSdkTooOldException) {
                message = "Please update this app"
                exception = e
            } catch (e: UnavailableDeviceNotCompatibleException) {
                message = "This device does not support AR"
                exception = e
            } catch (e: Exception) {
                message = "Failed to create AR session"
                exception = e
            }
            if (message != null) {
                messageSnackbarHelper.showError(requireActivity(), message)
                Log.e(
                    TAG,
                    "Exception creating session",
                    exception
                )
                return
            }


        }

        // Note that order matters - see the note in onPause(), the reverse applies here.
        // 순서가 중요 onPause()의 반대로 적용
        try {
            session!!.resume()
            session!!.pause()
            session!!.resume()
        } catch (e: CameraNotAvailableException) {
            messageSnackbarHelper.showError(
                requireActivity(),
                "Camera not available. Try restarting the app."
            )
            session = null
            return
        }
        surfaceView.onResume()
        displayRotationHelper!!.onResume()
        binding.apply {
            inputImageView.bringToFront()
            layoutTop.bringToFront()
            graphicOverlay.bringToFront()
        }
    }

    override fun onPause() {
        super.onPause()
        if (session != null) {
            // Note that the order matters - GLSurfaceView is paused first so that it does not try
            // to query the session. If Session is paused before GLSurfaceView, GLSurfaceView may
            // still call session.update() and get a SessionPausedException.
            // GLSurfaceView가 먼저 일시중지되어 세션 쿼리를 시도하지 않음
            // 세션이 GLSurfaceView 전에 일시중지된 경우 GLSurfaceView는 여전히 세션 호출 가능
            // update() 및 SessionPausedException을 가져옴
            displayRotationHelper!!.onPause()
            surfaceView.onPause()
            session!!.pause()
            tts.stop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.stop()

    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f)

        // Prepare the rendering objects. This involves reading shaders, so may throw an IOException.
        // 렌더링 개체 준비
        try {
            // The depth texture is used for object occlusion and rendering.
            depthTexture.createOnGlThread()

            // Create the texture and pass it to ARCore session to be filled during update().
            // 텍스처 생성 후 update() 중에 채워질 ARCore 세션에 전달
            backgroundRenderer.createOnGlThread( /*context=*/requireContext())
            backgroundRenderer.createDepthShaders( /*context=*/requireContext(),
                depthTexture.getDepthTexture()
            )
            //virtualObject.createOnGlThread( /*context=*/requireContext(), "models/andy.obj", "models/andy.png")
            //virtualObject.setMaterialProperties(0.0f, 2.0f, 0.5f, 6.0f)
        } catch (e: IOException) {
            Log.e(
                TAG,
                "Failed to read an asset file",
                e
            )
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        displayRotationHelper!!.onSurfaceChanged(width, height)
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        // Clear screen to notify driver it should not load any pixels from previous frame.
        // 이전 프레임에서 픽셀을 로드하지 않도록 드라이버에 알리기 위해 화면을 지운다.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        if (session == null) {
            return
        }
        // Notify ARCore session that the view size changed so that the perspective matrix and
        // the video background can be properly adjusted.
        displayRotationHelper!!.updateSessionIfNeeded(session!!)
        try {
            session!!.setCameraTextureName(backgroundRenderer.getTextureId())

            // Obtain the current frame from ARSession. When the configuration is set to
            // UpdateMode.BLOCKING (it is by default), this will throttle the rendering to the
            // camera framerate.
            val frame = session!!.update()
            val camera = frame.camera

            // Retrieves the latest depth image for this frame.
            // 이 프레임의 최신 깊이 이미지를 검색
            if (isDepthSupported) {
                depthTexture.update(frame)
            }
            // Handle one tap per frame.
            // If frame is ready, render camera preview image to the GL surface.
            // 프레임이 준비되면 카메라 미리보기 이미지를 GL 표면으로 렌더링
            backgroundRenderer.draw(frame)
            if (showDepthMap) {
                backgroundRenderer.drawDepth(frame)
            }

            // Keep the screen unlocked while tracking, but allow it to lock when tracking stops.
            trackingStateHelper.updateKeepScreenOnFlag(camera.trackingState)

            // If not tracking, don't draw 3D objects, show tracking failure reason instead.
            if (camera.trackingState == TrackingState.PAUSED) {
                messageSnackbarHelper.showMessage(
                    requireActivity(),
                    TrackingStateHelper(requireActivity()).getTrackingFailureReasonString(camera)!!
                )
                return
            }

            // Get projection matrix.
            val projmtx = FloatArray(16)
            camera.getProjectionMatrix(projmtx, 0, 0.1f, 100.0f)

            // Get camera matrix and draw.
            val viewmtx = FloatArray(16)
            camera.getViewMatrix(viewmtx, 0)

            // Compute lighting from average intensity of the image.
            // The first three components are color scaling factors.
            // The last one is the average pixel intensity in gamma space.
            val colorCorrectionRgba = FloatArray(4)
            frame.lightEstimate.getColorCorrection(colorCorrectionRgba, 0)

            try {
                // 현재 프레임에 해당하는 이미지 객체를 가져옴
                // 가로 모드
                val currentFrameImage = frame.acquireCameraImage()

                // 객체를 탐지할 비트맵
                // 세로 모드
                val bitmap = RotateBitmap(
                    imageToBitmap(currentFrameImage, requireContext())!!,
                    90f
                )
                // Depth 비트맵
                // 가로 모드
                val depthImage = frame.acquireDepthImage16Bits()

                gpuThread.execute {
                    // 비트맵에서 객체를 찾아 감지된 결과 리스트를 저장합니다.
                    val detectionResults = runObjectDetection(bitmap!!)

                    // 비트맵에 검출 결과를 그려서 보여줍니다
                    // 음성으로 출력
                    val imgWithResult = drawDetectionResult(bitmap, depthImage, detectionResults)
                    requireActivity().runOnUiThread {
                        binding.inputImageView.setImageBitmap(imgWithResult)
                    }
                    currentFrameImage.close()
                    depthImage.close()
                    bitmap.recycle()
                }

            } catch (e: Exception) {
                Log.d(TAG, "onDrawFrame: ${e.message}")
            }

            // No tracking error at this point. Inform user of what to do based on if planes are found.
            // Visualize anchors created by touch.
            val scaleFactor = 1.0f
            for (anchor in anchors) {
                if (anchor.trackingState != TrackingState.TRACKING) {
                    continue
                }
                // Get the current pose of an Anchor in world space. The Anchor pose is updated
                // during calls to session.update() as ARCore refines its estimate of the world.
                anchor.pose.toMatrix(anchorMatrix, 0)

                // Update and draw the model and its shadow.
                virtualObject.updateModelMatrix(anchorMatrix, scaleFactor)
                virtualObject.draw(
                    viewmtx,
                    projmtx,
                    colorCorrectionRgba,
                    OBJECT_COLOR
                )
            }


        } catch (t: Throwable) {
            // Avoid crashing the application due to unhandled exceptions.
            Log.e(
                TAG,
                "Exception on the OpenGL thread",
                t
            )
        }


    }

    private fun runObjectDetection(bitmap: Bitmap): List<DetectionResult> {
        // Step 1: Create TFLite's TensorImage object
        val image = TensorImage.fromBitmap(bitmap)
        // Step 3:주어진 이미지를 감지기에 공급
        val results = detector.detect(image)
        // Step 4: 탐지 결과를 파싱하여 보여줍니다.
        return results.map {
            // Get the top-1 category and craft the display text
            val category = it.categories.first()
            val text = category.label
            val score = category.score.times(100).toInt()
            // 탐지 결과를 표시할 데이터 객체 생성
            DetectionResult(it.boundingBox, text, score)
        }
    }

    private fun drawDetectionResult(
        bitmap: Bitmap,
        depthImage: Image,
        detectionResults: List<DetectionResult>
    ): Bitmap {
        val outputBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(outputBitmap)
        val pen = Paint()

        detectionResults.forEach {
            if (it.boundingBox.width() < 1000) {
                // 박스 그리기
                pen.color = Color.RED
                pen.strokeWidth = 8F
                pen.style = Paint.Style.STROKE
                val box = it.boundingBox
                canvas.drawRect(box, pen)

                // 중점 그리기
                canvas.drawCircle(it.boundingBox.centerX(), it.boundingBox.centerY(), 8F, pen)

                // 객체 이름, 점수 그리기
                pen.style = Paint.Style.FILL_AND_STROKE
                pen.color = Color.YELLOW
                pen.strokeWidth = 2F
                pen.textAlign = Paint.Align.LEFT
                pen.textSize = MAX_FONT_SIZE

                var showText = ""
                when(it.text){
                    "bicycle" -> showText = "자전거"
                    "kickboard" -> showText = "킥보드"
                    "crosswalk" -> showText = "횡단보도"
                }
                val objectText = "${showText}"

                val tagSize = Rect(0, 0, 0, 0)
                pen.getTextBounds(objectText, 0, objectText.length, tagSize)
                val fontSize: Float = pen.textSize * box.width() / tagSize.width()

                // adjust the font size so texts are inside the bounding box
                if (fontSize < pen.textSize) pen.textSize = fontSize

                var margin = (box.width() - tagSize.width()) / 2.0F
                if (margin < 0F) margin = 0F
                canvas.drawText(
                    objectText, box.left + margin,
                    box.top + tagSize.height().times(1F), pen
                )

                // 객체와의 거리 그리기
                val centerX = it.boundingBox.centerX().toInt()
                val centerY = it.boundingBox.centerY().toInt()

                val ratio = bitmap.height / depthImage.width
                val depthX = centerY / ratio
                val depthY = depthImage.height - (centerX / ratio)

                val distance = depthTexture.getMillimetersDepth(depthImage, depthX, depthY)

                var cm = (distance / 10.0).toFloat()
                var convertedDistance = ""
                if (cm > 100) {
                    cm /= 100
                    convertedDistance = "%.1f m".format(cm)
                } else {
                    convertedDistance = "${cm.toInt()} cm"
                }
                canvas.drawText(
                    convertedDistance, box.centerX(),
                    box.centerY(), pen
                )

                var location = ""
                val w = bitmap.width / 3
                val h = bitmap.height / 3

                if (centerX in 0..w) {
                    if (centerY in 0..h) {
                        location = "11시 방향"
                    } else if (centerY in h..h * 2) {
                        location = "9시 방향"
                    } else if (centerY in h * 2..bitmap.height) {
                        location = "7시 방향"
                    }
                } else if (centerX in w..w * 2) {
                    if (centerY in 0..h) {
                        location = "12시 방향"
                    } else if (centerY in h..h * 2) {
                        location = "중앙"
                    } else if (centerY in h * 2..bitmap.height) {
                        location = "6시 방향"
                    }
                } else if (centerX in w * 2..bitmap.width) {
                    if (centerY in 0..h) {
                        location = "1시 방향"
                    } else if (centerY in h..h * 2) {
                        location = "3시 방향"
                    } else if (centerY in h * 2..bitmap.height) {
                        location = "5시 방향"
                    }
                }



                // 음성 출력
                if (System.currentTimeMillis() - lastSpeakTime > INTERVAL) {
                    lastSpeakTime = System.currentTimeMillis()
                    speakOut("$location ${convertedDistance}에 ${showText}가 있습니다")
                }
                graphicOverlay.draw(canvas)
                graphicOverlay.bringToFront()
            }
        }
        return outputBitmap
    }

    override fun onInit(p0: Int) {
        if (p0 == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.KOREAN)
            tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(p0: String?) {}
                override fun onDone(p0: String?) {}
                override fun onError(p0: String?) {}
            })
        }
    }

    private fun speakOut(text: String) {
        tts.setPitch(1f)
        tts.setSpeechRate(3.5f)
        tts.speak(text, TextToSpeech.QUEUE_ADD, null, "id1")
    }
}

data class DetectionResult(val boundingBox: RectF, val text: String, val score: Int)