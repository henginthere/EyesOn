package com.d201.eyeson.view.blind.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d201.domain.usecase.user.DeleteUserUseCase
import com.d201.domain.utils.ResultType
import com.d201.eyeson.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlindSettingViewModel @Inject constructor(
    private val deleteUserUseCase: DeleteUserUseCase
): ViewModel() {

    private val _deleteUserEvent = SingleLiveEvent<Boolean>()
    val deleteUserEvent get() = _deleteUserEvent
    fun deleteUser(){
        viewModelScope.launch(Dispatchers.IO){
            deleteUserUseCase.excute().collectLatest {
                if (it is ResultType.Success){
                    _deleteUserEvent.postValue(true)
                }
            }
        }
    }
}