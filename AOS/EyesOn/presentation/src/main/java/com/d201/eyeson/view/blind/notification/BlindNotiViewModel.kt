package com.d201.eyeson.view.blind.notification

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d201.domain.model.Noti
import com.d201.domain.usecase.noti.SelectAllNotiUseCase
import com.d201.domain.utils.ResultType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "BlindNotiViewModel"
@HiltViewModel
class BlindNotiViewModel @Inject constructor(
    private val selectAllNotiUseCase: SelectAllNotiUseCase
) : ViewModel(){

    private val _notis : MutableStateFlow<ResultType<List<Noti>>> = MutableStateFlow(ResultType.Uninitialized)
    val notis get() = _notis.asStateFlow()

    fun selectAllNotis(){
        viewModelScope.launch(Dispatchers.IO) {
            selectAllNotiUseCase.execute().collectLatest {
                if(it is ResultType.Success){
                    _notis.value = it
                    Log.d(TAG, "selectAllNotis: ${it.data}")
                } else {
                    Log.d(TAG, "selectAllNotis: Error")
                }
            }
        }
    }
}