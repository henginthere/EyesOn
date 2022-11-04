package com.d201.data.datasource.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.d201.data.api.ComplaintsApi
import com.d201.data.mapper.mapperToListComplaints
import com.d201.data.model.response.ComplaintsResponse
import com.d201.data.utils.COMPLAINTS_PAGE_SIZE
import com.d201.data.utils.SELELCT_ALL
import com.d201.data.utils.SELELCT_BY_ANGEL
import com.d201.data.utils.SELELCT_BY_BLIND
import com.d201.domain.base.BaseResponse
import com.d201.domain.model.Complaints
import com.d201.domain.model.PagingResult
import javax.inject.Inject

private const val TAG ="ComplaintsPagingSource"
class ComplaintsPagingSource(private val complaintsApi: ComplaintsApi, private val flag: Int): PagingSource<Int, Complaints>() {

    override suspend fun load(params: LoadParams<Int>): PagingSource.LoadResult<Int, Complaints> {
        return try {
            val page = params.key ?: 0
            var response : BaseResponse<PagingResult<ComplaintsResponse>>? = null
            when(flag){
                SELELCT_ALL -> response = complaintsApi.selectAllComplaints(page, COMPLAINTS_PAGE_SIZE)
                SELELCT_BY_ANGEL -> response = complaintsApi.selectComplaintsByAngel(page, COMPLAINTS_PAGE_SIZE)
                SELELCT_BY_BLIND -> response = complaintsApi.selectComplaintsByBlind(page, COMPLAINTS_PAGE_SIZE)
            }
            PagingSource.LoadResult.Page(
                data = response!!.data.result.mapperToListComplaints(),
                prevKey = if(page == 0) null else page - 1,
                nextKey = if(page == response.data.totalPage) null else page + 1
            )
        }catch (e: Exception){
            Log.d(TAG, "load: ${e.message}")
            PagingSource.LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Complaints>): Int? {
        TODO("Not yet implemented")
    }
}
