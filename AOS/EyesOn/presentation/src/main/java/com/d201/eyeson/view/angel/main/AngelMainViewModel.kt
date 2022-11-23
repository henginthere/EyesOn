package com.d201.eyeson.view.angel.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.d201.domain.model.AngelInfo
import com.d201.domain.model.Complaints
import com.d201.domain.usecase.complaints.SelectAllCompUseCase
import com.d201.domain.usecase.complaints.SelectCompByAngelUseCase
import com.d201.domain.usecase.user.GetAngelInfoUseCase
import com.d201.domain.utils.ResultType
import com.d201.eyeson.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AngelMainViewModel"

@HiltViewModel
class AngelMainViewModel @Inject constructor(
    private val angelInfoUseCase: GetAngelInfoUseCase,
    private val selectCompByAngelUseCase: SelectCompByAngelUseCase,
    private val selectAllCompUseCase: SelectAllCompUseCase,
) : ViewModel() {

    private val _angelInfo: MutableStateFlow<AngelInfo?> = MutableStateFlow(null)
    val angelInfoData get() = _angelInfo.asStateFlow()

    private val _angelInfoSuccess = SingleLiveEvent<Boolean>()
    val angelInfoSuccess get() = _angelInfoSuccess

    fun getAngelInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            angelInfoUseCase.execute().collectLatest {
                if (it is ResultType.Success && it.data.status == 200) {
                    _angelInfo.value = it.data.data
                    _angelInfoSuccess.postValue(true)
                } else {
                    Log.d(TAG, "getAngelInfo: ${it}")
                }
            }
        }
    }

    private val _complaintsList: MutableStateFlow<PagingData<Complaints>?> = MutableStateFlow(null)
    val complaintsList get() = _complaintsList.asStateFlow()

    fun getComplaintsByAngelList() {
        viewModelScope.launch(Dispatchers.IO) {
            selectCompByAngelUseCase.execute().cachedIn(this).collectLatest {
                _complaintsList.value = it
            }
        }
    }

    fun getComplaintsList() {
        viewModelScope.launch(Dispatchers.IO) {
            selectAllCompUseCase.execute().cachedIn(this).collectLatest {
                _complaintsList.value = it
            }
        }
    }

//    private val _complaintsList: MutableStateFlow<Int> = MutableStateFlow(0)
//    val complaintsList = _complaintsList.switchMap {
//        selectAllCompUseCase.excute(it).
//    }
//    fun selectComplaints(flag: Int){
//
//    }

}