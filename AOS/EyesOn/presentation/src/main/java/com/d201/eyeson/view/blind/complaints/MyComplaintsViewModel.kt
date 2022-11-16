package com.d201.eyeson.view.blind.complaints

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.d201.domain.model.Complaints
import com.d201.domain.usecase.complaints.SelectCompByBlindUseCase
import com.d201.domain.usecase.complaints.SelectCompBySeqUseCase
import com.d201.domain.utils.ResultType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MyComplaintsViewModel"
@HiltViewModel
class MyComplaintsViewModel @Inject constructor(
    private val selectCompBySeqUseCase: SelectCompBySeqUseCase,
    private val selectCompByBlindUseCase: SelectCompByBlindUseCase): ViewModel() {
    fun getComplaintsList(): Flow<PagingData<Complaints>> {
        return selectCompByBlindUseCase.execute()
    }

    private val _complaints: MutableStateFlow<Complaints?> = MutableStateFlow(null)
    val complaints get() = _complaints.asStateFlow()
    fun getComplaints(complaintsSeq: Long){
        viewModelScope.launch(Dispatchers.IO){
            selectCompBySeqUseCase.execute(complaintsSeq).collectLatest {
                when(it){
                    is ResultType.Success -> {
                        var tmp = it.data.data
                        when(tmp.state){
                            "PROGRESS_IN" -> { tmp.state = "민원 처리중" }
                            "RETURN" -> { tmp.state = "민원 반환됨" }
                            "REGIST_DONE" -> { tmp.state = "민원 등록 완료" }
                            "PROGRESS_DONE" -> { tmp.state = "민원 처리 완료" }
                        }
                        _complaints.value = tmp
                    }
                    else -> Log.d(TAG, "getComplaints: ${it}")
                }
            }
        }
    }
}