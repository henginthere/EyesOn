package com.d201.eyeson.view.blind.findobject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import javax.inject.Inject

private const val TAG = "FindObjectViewModel"
@HiltViewModel
class FindObjectViewModel @Inject constructor(): ViewModel(){

    //STT
    private lateinit var speechRecognizer: SpeechRecognizer

    private val _recordText: MutableStateFlow<String> = MutableStateFlow("")
    val recordText get() = _recordText

    private val _statusSTT: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val statusSTT get() = _statusSTT

    fun startRecord(context: Context){
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra("android.speech.extra.DICTATION_MODE", true)
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        speechRecognizer.apply {
            setRecognitionListener(recognitionListener())
            startListening(speechRecognizerIntent)
        }
    }

    private fun recognitionListener() = object :
        RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {_statusSTT.value = true }
        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onPartialResults(partialResults: Bundle?) {}
        override fun onEvent(eventType: Int, params: Bundle?) {}
        override fun onBeginningOfSpeech() {}
        override fun onEndOfSpeech() {}
        override fun onError(error: Int) {_statusSTT.value = false}
        override fun onResults(results: Bundle) {
            val result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)!![0]
            _recordText.value = result
            _statusSTT.value = false
        }
    }
}