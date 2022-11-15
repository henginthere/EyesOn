package com.practice.mlkit

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.mlkit.common.MlKitException
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.demo.kotlin.objectdetector.ObjectDetectorProcessor
import com.google.mlkit.vision.demo.kotlin.textdetector.TextRecognitionProcessor
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.practice.mlkit.common.*
import com.practice.mlkit.databinding.ActivityMainBinding
import com.practice.mlkit.mlkit.CameraXViewModel
import com.practice.mlkit.mlkit.GraphicOverlay
import com.practice.mlkit.mlkit.PreferenceUtils
import com.practice.mlkit.mlkit.VisionImageProcessor

private const val TAG ="MainActivity"
class MainActivity : AppCompatActivity() {
    private var previewView: PreviewView? = null
    private var graphicOverlay: GraphicOverlay? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var previewUseCase: Preview? = null
    private var analysisUseCase: ImageAnalysis? = null
    private var imageProcessor: VisionImageProcessor? = null
    private var needUpdateGraphicOverlayImageSourceInfo = false
    private var selectedModel = OBJECT_DETECTION
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var cameraSelector: CameraSelector? = null

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(R.id.frame_main, CameraPreviewFragment()).commit()


//        if (savedInstanceState != null) {
//            selectedModel = savedInstanceState.getString(STATE_SELECTED_MODEL, OBJECT_DETECTION)
//        }
//        cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
//        setContentView(binding.root)
//
//        previewView = binding.previewView
//        graphicOverlay = binding.graphicOverlay
//        binding.facingSwitch.setOnClickListener {  }
//        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[CameraXViewModel::class.java]
//            .processCameraProvider
//            .observe(
//                this,
//                Observer { provider: ProcessCameraProvider? ->
//                    cameraProvider = provider
//                    bindAllCameraUseCases()
//                }
//            )

    }

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        bundle.putString(STATE_SELECTED_MODEL, selectedModel)
    }

    public override fun onResume() {
        super.onResume()
//        bindAllCameraUseCases()
    }

    override fun onPause() {
        super.onPause()

//        imageProcessor?.run { this.stop() }
    }

    public override fun onDestroy() {
        super.onDestroy()
//        imageProcessor?.run { this.stop() }
    }

    private fun bindAllCameraUseCases() {
        if (cameraProvider != null) {
            // As required by CameraX API, unbinds all use cases before trying to re-bind any of them.
            cameraProvider!!.unbindAll()
            bindPreviewUseCase()
            bindAnalysisUseCase()
        }
    }

    private fun bindPreviewUseCase() {
        if (!PreferenceUtils.isCameraLiveViewportEnabled(this)) {
            return
        }
        if (cameraProvider == null) {
            return
        }
        if (previewUseCase != null) {
            cameraProvider!!.unbind(previewUseCase)
        }

        val builder = Preview.Builder()
        val targetResolution = PreferenceUtils.getCameraXTargetResolution(this, lensFacing)
        if (targetResolution != null) {
            builder.setTargetResolution(targetResolution)
        }
        previewUseCase = builder.build()
        previewUseCase!!.setSurfaceProvider(previewView!!.getSurfaceProvider())
        cameraProvider!!.bindToLifecycle(/* lifecycleOwner= */ this, cameraSelector!!, previewUseCase)
    }

    private fun bindAnalysisUseCase() {
        if (cameraProvider == null) {
            return
        }
        if (analysisUseCase != null) {
            cameraProvider!!.unbind(analysisUseCase)
        }
        if (imageProcessor != null) {
            imageProcessor!!.stop()
        }
        imageProcessor =
            try {
                when (selectedModel) {
                    OBJECT_DETECTION -> {
                        Log.i(TAG, "Using Object Detector Processor")
                        val objectDetectorOptions = PreferenceUtils.getObjectDetectorOptionsForLivePreview(this)
                        ObjectDetectorProcessor(this, objectDetectorOptions)
                    }
                    OBJECT_DETECTION_CUSTOM -> {
                        Log.i(TAG, "Using Custom Object Detector (with object labeler) Processor")
                        val localModel =
                            LocalModel.Builder().setAssetFilePath("custom_models/object_labeler.tflite").build()
                        val customObjectDetectorOptions =
                            PreferenceUtils.getCustomObjectDetectorOptionsForLivePreview(this, localModel)
                        ObjectDetectorProcessor(this, customObjectDetectorOptions)
                    }
                    CUSTOM_AUTOML_OBJECT_DETECTION -> {
                        Log.i(TAG, "Using Custom AutoML Object Detector Processor")
                        val customAutoMLODTLocalModel =
                            LocalModel.Builder().setAssetManifestFilePath("automl/manifest.json").build()
                        val customAutoMLODTOptions =
                            PreferenceUtils.getCustomObjectDetectorOptionsForLivePreview(
                                this,
                                customAutoMLODTLocalModel
                            )
                        ObjectDetectorProcessor(this, customAutoMLODTOptions)
                    }
                    TEXT_RECOGNITION_KOREAN -> {
                        Log.i(TAG, "Using on-device Text recognition Processor for Latin and Korean")
                        TextRecognitionProcessor(this, KoreanTextRecognizerOptions.Builder().build())
                    }
                    else -> throw IllegalStateException("Invalid model name")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Can not create image processor: $selectedModel", e)
                Toast.makeText(
                    applicationContext,
                    "Can not create image processor: " + e.localizedMessage,
                    Toast.LENGTH_LONG
                )
                    .show()
                return
            }

        val builder = ImageAnalysis.Builder()
        val targetResolution = PreferenceUtils.getCameraXTargetResolution(this, lensFacing)
        if (targetResolution != null) {
            builder.setTargetResolution(targetResolution)
        }
        analysisUseCase = builder.build()

        needUpdateGraphicOverlayImageSourceInfo = true

        analysisUseCase?.setAnalyzer(
            // imageProcessor.processImageProxy will use another thread to run the detection underneath,
            // thus we can just runs the analyzer itself on main thread.
            ContextCompat.getMainExecutor(this),
            ImageAnalysis.Analyzer { imageProxy: ImageProxy ->
                if (needUpdateGraphicOverlayImageSourceInfo) {
                    val isImageFlipped = lensFacing == CameraSelector.LENS_FACING_FRONT
                    val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                    if (rotationDegrees == 0 || rotationDegrees == 180) {
                        graphicOverlay!!.setImageSourceInfo(imageProxy.width, imageProxy.height, isImageFlipped)
                    } else {
                        graphicOverlay!!.setImageSourceInfo(imageProxy.height, imageProxy.width, isImageFlipped)
                    }
                    needUpdateGraphicOverlayImageSourceInfo = false
                }
                try {
                    imageProcessor!!.processImageProxy(imageProxy, graphicOverlay)
                } catch (e: MlKitException) {
                    Log.e(TAG, "Failed to process image. Error: " + e.localizedMessage)
                    Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        )
        cameraProvider!!.bindToLifecycle(/* lifecycleOwner= */ this, cameraSelector!!, analysisUseCase)
    }

}