package com.d201.eyeson.view.angel.complaints

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d201.domain.model.Complaints
import com.d201.domain.usecase.complaints.ReturnCompUseCase
import com.d201.domain.usecase.complaints.SelectCompBySeqUseCase
import com.d201.domain.usecase.complaints.SubmitCompUseCase
import com.d201.domain.utils.ResultType
import com.d201.eyeson.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ComplaintsViewModel"
@HiltViewModel
class ComplaintsViewModel @Inject constructor(
    private val selectCompBySeqUseCase: SelectCompBySeqUseCase,
    private val returnCompUseCase: ReturnCompUseCase,
    private val submitCompUseCase: SubmitCompUseCase
    ) : ViewModel() {

    private val _successResultEvent = SingleLiveEvent<String>()
    val successResultEvent get() = _successResultEvent

    private val _errorResultEvent = SingleLiveEvent<String>()
    val errorResultEvent get() = _errorResultEvent

    private val _complaints: MutableStateFlow<Complaints?> = MutableStateFlow(null)
    val complaints get() = _complaints.asStateFlow()
    fun getComplaints(complaintsSeq: Long){
        viewModelScope.launch(Dispatchers.IO){
            selectCompBySeqUseCase.excute(complaintsSeq).collectLatest {
                when(it){
                    is ResultType.Success -> {
                        _complaints.value = it.data.data
                    }
                    else -> Log.d(TAG, "getComplaints: ${it}")
                }
            }
        }
    }

    fun returnComplaints(returnComplaints: Complaints){
        viewModelScope.launch(Dispatchers.IO){
            returnCompUseCase.excute(returnComplaints).collectLatest {
                when(it){
                    is ResultType.Success -> { _successResultEvent.postValue(it.data.message) }
                    is ResultType.Error -> { _successResultEvent.postValue(it.exception.message)}
                    else -> { Log.d(TAG, "returnComplaints: ${it}")}
                }
            }
        }
    }

    fun submitComplaints(submitComplaints: Complaints){
        viewModelScope.launch(Dispatchers.IO){
            submitCompUseCase.excute(submitComplaints).collectLatest {
                when(it){
                    is ResultType.Success -> { _successResultEvent.postValue(it.data.message) }
                    is ResultType.Error -> { _successResultEvent.postValue(it.exception.message)}
                    else -> { Log.d(TAG, "returnComplaints: ${it}")}
                }
            }
        }
    }


}