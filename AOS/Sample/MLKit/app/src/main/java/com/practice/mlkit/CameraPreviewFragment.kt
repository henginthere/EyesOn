package com.practice.mlkit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.demo.kotlin.objectdetector.ObjectDetectorProcessor
import com.google.mlkit.vision.demo.kotlin.textdetector.TextRecognitionProcessor
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.practice.mlkit.common.*
import com.practice.mlkit.databinding.FragmentCameraPreviewBinding
import com.practice.mlkit.mlkit.CameraSource
import com.practice.mlkit.mlkit.GraphicOverlay
import com.practice.mlkit.mlkit.PreferenceUtils
import java.io.IOException

private const val TAG = "CameraPreviewFragment"
class CameraPreviewFragment : Fragment(), CompoundButton.OnCheckedChangeListener {
    private var cameraSource: CameraSource? = null
    private var preview: CameraSourcePreview? = null
    private var graphicOverlay: GraphicOverlay? = null
    private var selectedModel = OBJECT_DETECTION_CUSTOM

    private lateinit var binding: FragmentCameraPreviewBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preview = binding.previewView

        if (preview == null) {
            Log.d(TAG, "Preview is null")
        }

        graphicOverlay = binding.graphicOverlay
        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null")
        }

        val facingSwitch = binding.facingSwitch
        facingSwitch.setOnCheckedChangeListener(this)

        val settingsButton = binding.settingsButton
        settingsButton.setOnClickListener {
//            val intent = Intent(requireContext(), SettingsActivity::class.java)
//            intent.putExtra(SettingsActivity.EXTRA_LAUNCH_SOURCE, LaunchSource.LIVE_PREVIEW)
//            startActivity(intent)
        }

        createCameraSource(selectedModel)
    }
    private fun createCameraSource(model: String) {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = CameraSource(requireActivity(), graphicOverlay)
        }
        try {
            when (model) {
                OBJECT_DETECTION -> {
                    Log.i(TAG, "Using Object Detector Processor")
                    val objectDetectorOptions = PreferenceUtils.getObjectDetectorOptionsForLivePreview(requireContext())
                    cameraSource!!.setMachineLearningFrameProcessor(
                        ObjectDetectorProcessor(requireContext(), objectDetectorOptions)
                    )
                }
                OBJECT_DETECTION_CUSTOM -> {
                    Log.i(TAG, "Using Custom Object Detector Processor")
                    val localModel =
                        LocalModel.Builder().setAssetFilePath("object_labeler.tflite").build()
                    val customObjectDetectorOptions =
                        PreferenceUtils.getCustomObjectDetectorOptionsForLivePreview(requireContext(), localModel)
                    cameraSource!!.setMachineLearningFrameProcessor(
                        ObjectDetectorProcessor(requireContext(), customObjectDetectorOptions)
                    )
                }
                CUSTOM_AUTOML_OBJECT_DETECTION -> {
                    Log.i(TAG, "Using Custom AutoML Object Detector Processor")
                    val customAutoMLODTLocalModel =
                        LocalModel.Builder().setAssetManifestFilePath("automl/manifest.json").build()
                    val customAutoMLODTOptions =
                        PreferenceUtils.getCustomObjectDetectorOptionsForLivePreview(
                            requireContext(),
                            customAutoMLODTLocalModel
                        )
                    cameraSource!!.setMachineLearningFrameProcessor(
                        ObjectDetectorProcessor(requireContext(), customAutoMLODTOptions)
                    )
                }
                TEXT_RECOGNITION_LATIN -> {
                    Log.i(TAG, "Using on-device Text recognition Processor for Latin and Latin")
                    cameraSource!!.setMachineLearningFrameProcessor(
                        TextRecognitionProcessor(requireContext(), TextRecognizerOptions.Builder().build())
                    )
                }

                TEXT_RECOGNITION_KOREAN -> {
                    Log.i(TAG, "Using on-device Text recognition Processor for Latin and Korean")
                    cameraSource!!.setMachineLearningFrameProcessor(
                        TextRecognitionProcessor(requireContext(), KoreanTextRecognizerOptions.Builder().build())
                    )
                }

                else -> Log.e(TAG, "Unknown model: $model")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Can not create image processor: $model", e)
            Toast.makeText(
                requireContext(),
                "Can not create image processor: " + e.message,
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    private fun startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d(TAG, "resume: Preview is null")
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null")
                }
                preview!!.start(cameraSource, graphicOverlay)
            } catch (e: IOException) {
                Log.e(TAG, "Unable to start camera source.", e)
                cameraSource!!.release()
                cameraSource = null
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        createCameraSource(selectedModel)
        startCameraSource()
    }

    /** Stops the camera. */
    override fun onPause() {
        super.onPause()
        preview?.stop()
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (cameraSource != null) {
            cameraSource?.release()
        }
    }

    override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
        Log.d(TAG, "Set facing")
        if (cameraSource != null) {
            if (isChecked) {
                cameraSource?.setFacing(CameraSource.CAMERA_FACING_FRONT)
            } else {
                cameraSource?.setFacing(CameraSource.CAMERA_FACING_BACK)
            }
        }
        preview?.stop()
        startCameraSource()
    }

}
























