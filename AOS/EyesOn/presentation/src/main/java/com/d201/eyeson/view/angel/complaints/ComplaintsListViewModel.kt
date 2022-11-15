package com.d201.eyeson.view.angel.complaints

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.d201.domain.model.Complaints
import com.d201.domain.usecase.complaints.SelectAllCompUseCase
import com.d201.domain.usecase.complaints.SelectCompByAngelUseCase
import com.d201.domain.usecase.user.GetAngelInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG ="ComplaintsListViewModel"
@HiltViewModel
class ComplaintsListViewModel @Inject constructor(
    private val selectCompByAngelUseCase: SelectCompByAngelUseCase,
    private val selectAllCompUseCase: SelectAllCompUseCase
): ViewModel(){

    private val _complaintsList: MutableStateFlow<PagingData<Complaints>?> = MutableStateFlow(null)
    val complaintsList get() = _complaintsList.asStateFlow()

    fun getComplaintsByAngelList(){
        viewModelScope.launch(Dispatchers.IO){
            selectCompByAngelUseCase.excute().cachedIn(this).collectLatest {
                _complaintsList.value = it
            }
        }
    }

    fun getComplaintsList(){
        viewModelScope.launch(Dispatchers.IO){
            selectAllCompUseCase.excute().cachedIn(this).collectLatest {
                _complaintsList.value = it
            }
        }
    }
}