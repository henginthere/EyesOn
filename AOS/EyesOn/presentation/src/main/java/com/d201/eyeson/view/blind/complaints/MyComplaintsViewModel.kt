package com.d201.eyeson.view.blind.complaints

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.d201.domain.model.Complaints
import com.d201.domain.usecase.complaints.SelectCompByBlindUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MyComplaintsViewModel @Inject constructor(private val selectCompByBlindUseCase: SelectCompByBlindUseCase): ViewModel() {
    fun getComplaintsList(): Flow<PagingData<Complaints>> {
        return selectCompByBlindUseCase.excute()
    }
}