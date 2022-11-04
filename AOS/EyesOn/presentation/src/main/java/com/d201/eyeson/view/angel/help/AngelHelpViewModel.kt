package com.d201.eyeson.view.angel.help

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.d201.eyeson.util.JWT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AngelHelpViewModel @Inject constructor(
    private val sharedPref : SharedPreferences
) : ViewModel() {

    private val _sessionId : MutableStateFlow<String> = MutableStateFlow("")
    val sessionId get() = _sessionId.asStateFlow()

    fun getSessionId(){
        _sessionId.value = sharedPref.getString(JWT, "")!!
    }
}