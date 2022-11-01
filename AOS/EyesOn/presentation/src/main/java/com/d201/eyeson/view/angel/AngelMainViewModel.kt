package com.d201.eyeson.view.angel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d201.domain.model.AngelInfo
import com.d201.domain.usecase.user.GetAngelInfoUseCase
import com.d201.domain.utils.ResultType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG ="AngelMainViewModel"
@HiltViewModel
class AngelMainViewModel @Inject constructor(private val angelInfoUseCase: GetAngelInfoUseCase): ViewModel(){

    private val _angelInfo: MutableStateFlow<AngelInfo?> = MutableStateFlow(null)
    val angelInfo get() = _angelInfo.asStateFlow()

    fun getAngelInfo(){
        viewModelScope.launch(Dispatchers.IO){
            angelInfoUseCase.excute().collectLatest {
                Log.d(TAG, "getAngelInfo: ${it}")
                if(it is ResultType.Success && it.data.status == 200){
                    _angelInfo.value = it.data.data
                }else{
                    Log.d(TAG, "getAngelInfo: ${it}")
                }
            }
        }
    }
}