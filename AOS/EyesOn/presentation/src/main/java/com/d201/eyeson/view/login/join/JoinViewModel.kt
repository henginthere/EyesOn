package com.d201.eyeson.view.login.join

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d201.domain.model.Login
import com.d201.domain.usecase.PutUserRoleUseCase
import com.d201.domain.utils.ResultType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SelectGenderViewModel"
@HiltViewModel
class SelectGenderViewModel @Inject constructor(private val putUserRoleUseCase: PutUserRoleUseCase): ViewModel() {

    private val _info: MutableStateFlow<Login?> = MutableStateFlow(null)
    val info get() = _info.asStateFlow()

    fun putUserRole(role: String, gender: String){
        viewModelScope.launch(Dispatchers.IO){
            putUserRoleUseCase.execute(role, gender).collectLatest {
                if(it is ResultType.Success && it.data.status == 200){
                    _info.value = it.data.data
                }else{
                    Log.d(TAG, "putUserRole: ${it}")
                }
            }
        }
    }

}