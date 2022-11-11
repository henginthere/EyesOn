package com.d201.eyeson.view.blind.scantext

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import com.d201.arcore.depth.common.TEXT_RECOGNITION_KOREAN
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentScanTextBinding
import com.d201.mlkit.CameraSource
import com.d201.mlkit.CameraSourcePreview
import com.d201.mlkit.GraphicOverlay
import com.google.mlkit.vision.demo.kotlin.textdetector.TextRecognitionProcessor
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.*

private const val TAG = "ScanTextFragment"
private const val INTERVAL = 1000
@AndroidEntryPoint
class ScanTextFragment : BaseFragment<FragmentScanTextBinding>(R.layout.fragment_scan_text), TextToSpeech.OnInitListener {

    private var cameraSource: CameraSource? = null
    private var preview: CameraSourcePreview? = null
    private var graphicOverlay: GraphicOverlay? = null
    private var selectedModel = TEXT_RECOGNITION_KOREAN
    private lateinit var textRecognitionProcessor :TextRecognitionProcessor

    private lateinit var tts: TextToSpeech
    private var lastSpeakTime = 0L

    override fun init() {

        tts = TextToSpeech(requireContext(), this)
        preview = binding.previewView
        graphicOverlay = binding.graphicOverlay
        createCameraSource(selectedModel)
        lastSpeakTime = System.currentTimeMillis()
        initView()
    }

    private fun initView(){
        binding.apply {
            btnBack.setOnClickListener { requireActivity().finish() }
            layoutContent.setOnClickListener {
                tts.stop()
                lastSpeakTime -= 3000
            }
        }
    }

    private fun createCameraSource(model: String) {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = CameraSource(requireActivity(), graphicOverlay)
        }
        try {
            when (model) {
                TEXT_RECOGNITION_KOREAN -> {
                    Log.i(TAG, "Using on-device Text recognition Processor for Latin and Korean")
                    cameraSource!!.setMachineLearningFrameProcessor(
                        textRecognitionProcessor
                    )
                }
                else -> Log.e(TAG, "Unknown model: $model")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Can not create image processor: $model", e)
        }
    }

    /**
     * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
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

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        textRecognitionProcessor = TextRecognitionProcessor(requireContext(), KoreanTextRecognizerOptions.Builder().build())
        textRecognitionProcessor.textLiveData.observe(viewLifecycleOwner){
            if(System.currentTimeMillis() - lastSpeakTime > INTERVAL){
                lastSpeakTime = System.currentTimeMillis()
                if(!tts.isSpeaking){
                    binding.tvRecognizeText.text = it
                    speakOut(it.replace("\\r\\n|\\r|\\n|\\n\\r".toRegex()," "))
                }

            }
        }
        createCameraSource(selectedModel)
        startCameraSource()
    }

    /** Stops the camera. */
    override fun onPause() {
        super.onPause()
        preview?.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (cameraSource != null) {
            cameraSource?.release()
        }
        tts.stop()
    }

    override fun onInit(p0: Int) {
        if(p0 == TextToSpeech.SUCCESS) {
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