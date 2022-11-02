package com.d201.eyeson.view.angel.setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d201.domain.model.AngelInfo
import com.d201.domain.usecase.PutAngelInfoUseCase
import com.d201.domain.utils.ResultType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AngelSettingViewModel"
@HiltViewModel
class AngelSettingViewModel @Inject constructor(private val putAngelInfoUseCase: PutAngelInfoUseCase): ViewModel() {

    private val _angelInfo: MutableStateFlow<AngelInfo?> = MutableStateFlow(null)
    val angelInfo get() = _angelInfo.asStateFlow()

    fun putAngelInfo(alarmStart: Int, alarmEnd: Int, alarmDay: Int, active: Boolean){
        viewModelScope.launch(Dispatchers.IO){
            putAngelInfoUseCase.execute(AngelInfo(alarmStart, alarmEnd, alarmDay, active)).collectLatest {
                if(it is ResultType.Success && it.data.status == 200){
                    _angelInfo.value = it.data.data
                }else{
                    Log.d(TAG, "putAngelInfo: ${it}")
                }
            }
        }
    }
}