package com.d201.data.datasource.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.d201.data.api.ComplaintsApi
import com.d201.data.mapper.mapperToListComplaints
import com.d201.data.model.response.ComplaintsResponse
import com.d201.domain.base.BaseResponse
import com.d201.domain.model.Complaints
import com.d201.domain.model.PagingResult

class ComplaintsPagingSource(private val complaintsApi: ComplaintsApi, private val flag: Int): PagingSource<Int, Complaints>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Complaints> {
        return try {
            val page = params.key ?: 0
            var response : BaseResponse<PagingResult<ComplaintsResponse>>? = null
            when(flag){
                0 -> response = complaintsApi.selectAllComplaints(page, 10)
                1 -> response = complaintsApi.selectComplaintsByAngel(page, 10)
                2 -> response = complaintsApi.selectComplaintsByBlind(page, 10)
            }

            LoadResult.Page(
                data = response!!.data.list.mapperToListComplaints(),
                prevKey = if(page == 0) null else page - 1,
                nextKey = if(page == response.data.totalPage) null else page + 1
            )
        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Complaints>): Int? {
        TODO("Not yet implemented")
    }
}
