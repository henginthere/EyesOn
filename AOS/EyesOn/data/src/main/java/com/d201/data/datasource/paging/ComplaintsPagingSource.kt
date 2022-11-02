package com.d201.data.datasource.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.d201.data.api.ComplaintsApi
import com.d201.data.mapper.mapperToListComplaints
import com.d201.data.model.response.ComplaintsResponse
import com.d201.domain.base.BaseResponse
import com.d201.domain.model.Complaints
import com.d201.domain.model.PagingResult
import javax.inject.Inject

class ComplaintsPagingSource(private val flag: Int): PagingSource<Int, Complaints>() {

    @Inject
    lateinit var complaintsApi : ComplaintsApi

    override suspend fun load(params: LoadParams<Int>): PagingSource.LoadResult<Int, Complaints> {
        return try {
            val page = params.key ?: 0
            var response : BaseResponse<PagingResult<ComplaintsResponse>>? = null
            when(flag){
                0 -> response = complaintsApi.selectAllComplaints(page, 10)
                1 -> response = complaintsApi.selectComplaintsByAngel(page, 10)
                2 -> response = complaintsApi.selectComplaintsByBlind(page, 10)
            }

            PagingSource.LoadResult.Page(
                data = response!!.data.list.mapperToListComplaints(),
                prevKey = if(page == 0) null else page - 1,
                nextKey = if(page == response.data.totalPage) null else page + 1
            )
        }catch (e: Exception){
            PagingSource.LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Complaints>): Int? {
        TODO("Not yet implemented")
    }
}
