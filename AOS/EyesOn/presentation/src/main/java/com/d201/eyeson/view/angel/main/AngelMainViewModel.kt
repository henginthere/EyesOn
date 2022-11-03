package com.d201.eyeson.view.angel.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.d201.domain.model.AngelInfo
import com.d201.domain.model.Complaints
import com.d201.domain.usecase.complaints.SelectAllCompUseCase
import com.d201.domain.usecase.user.GetAngelInfoUseCase
import com.d201.domain.utils.ResultType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG ="AngelMainViewModel"
@HiltViewModel
class AngelMainViewModel @Inject constructor(private val angelInfoUseCase: GetAngelInfoUseCase, private val selectAllCompUseCase: SelectAllCompUseCase): ViewModel(){

    private val _angelInfo: MutableStateFlow<AngelInfo?> = MutableStateFlow(null)
    val angelInfo get() = _angelInfo.asStateFlow()

    fun getAngelInfo(){
        viewModelScope.launch(Dispatchers.IO){
            angelInfoUseCase.excute().collectLatest {
                if(it is ResultType.Success && it.data.status == 200){
                    _angelInfo.value = it.data.data
                }else{
                    Log.d(TAG, "getAngelInfo: ${it}")
                }
            }
        }
    }

    fun getComplaintsList(flag: Int): Flow<PagingData<Complaints>> {
        return selectAllCompUseCase.excute(flag)
    }

//    private val _complaintsList: MutableStateFlow<Int> = MutableStateFlow(0)
//    val complaintsList = _complaintsList.switchMap {
//        selectAllCompUseCase.excute(it).
//    }
//    fun selectComplaints(flag: Int){
//
//    }

}