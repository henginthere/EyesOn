package com.d201.eyeson.view.blind.complaints

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d201.data.mapper.mapperToComplaintsRequest
import com.d201.domain.model.Complaints
import com.d201.domain.usecase.complaints.InsertCompUseCase
import com.d201.domain.utils.ResultType
import com.d201.eyeson.util.LocationService
import com.d201.eyeson.util.SingleLiveEvent
import com.d201.eyeson.util.imagePathToPartBody
import com.d201.eyeson.util.objectToMultipartPart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import javax.inject.Inject

private const val TAG = "ComplaintsSubmitRecordViewModel"

@HiltViewModel
class ComplaintsSubmitRecordViewModel @Inject constructor(private val insertCompUseCase: InsertCompUseCase) :
    ViewModel(), TextToSpeech.OnInitListener {

    private val _successResultEvent = SingleLiveEvent<String>()
    val successResultEvent get() = _successResultEvent

    fun submitComplaints(complaints: Complaints, imagePath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val file = File(imagePath)
            insertCompUseCase.execute(
                complaints.objectToMultipartPart(complaints.mapperToComplaintsRequest()),
                imagePath.imagePathToPartBody("file", file)
            ).collectLatest {
                when (it) {
                    is ResultType.Success -> _successResultEvent.postValue(it.data.message)
                    else -> Log.d(TAG, "submitComplaints: ${it}")
                }
            }
        }
    }

    // GPS
    private val _location: MutableLiveData<Location>? = MutableLiveData<Location>()
    val location: LiveData<Location>?
        get() = _location
    var locationRepository: LocationService? = null

    fun setLocationRepository(context: Context) {
        locationRepository = LocationService.getInstance(context)
    }

    fun setLocationItem(location: Location) {
        _location!!.value = location
    }

    fun enableLocationServices() {
        locationRepository?.let {
            it.startService()
        }
    }


    //STT
    private lateinit var speechRecognizer: SpeechRecognizer

    private val _recordText: MutableStateFlow<String> = MutableStateFlow("안녕하세요")
    val recordText get() = _recordText

    private val _statusSTT: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val statusSTT get() = _statusSTT

    fun startRecord(context: Context) {
        stopTTS()
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
        override fun onReadyForSpeech(params: Bundle?) {
            _statusSTT.value = true
        }

        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onPartialResults(partialResults: Bundle?) {}
        override fun onEvent(eventType: Int, params: Bundle?) {}
        override fun onBeginningOfSpeech() {}
        override fun onEndOfSpeech() {}
        override fun onError(error: Int) {
            _statusSTT.value = false
        }

        override fun onResults(results: Bundle) {
            val result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)!![0]
            _recordText.value = result
            _statusSTT.value = false
            speakOut("입력하신 내용이 ${result}가 맞다면 전송하기 버튼을 눌러주세요 다시 녹음하시려면 녹음 버튼을 눌러주세요")
        }
    }

    private lateinit var tts: TextToSpeech
    fun initTTS(context: Context) {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(p0: Int) {
        if (p0 == TextToSpeech.SUCCESS) {
            tts.language = Locale.KOREAN
        }
    }

    private fun speakOut(text: String) {
        tts.setPitch(1f)
        tts.setSpeechRate(3.5f)
        tts.speak(text, TextToSpeech.QUEUE_ADD, null, "id1")
    }

    private fun stopTTS() {
        tts.stop()
    }
}