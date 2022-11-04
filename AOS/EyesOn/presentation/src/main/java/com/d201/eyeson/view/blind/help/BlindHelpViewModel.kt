package com.d201.eyeson.view.blind.help

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d201.domain.usecase.help.RequestHelpUseCase
import com.d201.domain.utils.ResultType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "BlindHelpViewModel"
@HiltViewModel
class BlindHelpViewModel @Inject constructor(
    private val requestHelpUseCase: RequestHelpUseCase
) : ViewModel() {

    private val _sessionId: MutableStateFlow<Int> = MutableStateFlow(-1)
    val sessionId get() = _sessionId.asStateFlow()

    fun requestHelp(gender: String) {
        viewModelScope.launch(Dispatchers.IO) {
            requestHelpUseCase.execute(gender).collectLatest {
                Log.d(TAG, "requestHelp: ${it.javaClass}")
                if(it is ResultType.Success){
                    Log.d(TAG, "requestHelp: ${it.data.data}")
                    _sessionId.value = it.data.data
                } else {
                    Log.d(TAG, "requestHelp: $it")
                }
            }
        }
    }
}