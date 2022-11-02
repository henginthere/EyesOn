package com.d201.data.datasource.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.d201.data.api.ComplaintsApi
import com.d201.data.mapper.mapperToListComplaints
import com.d201.domain.model.Complaints

private const val TAG ="ComplaintsPagingSource"
class ComplaintsPagingSource(private val complaintsApi: ComplaintsApi): PagingSource<Int, Complaints>() {

    var flag = 0
    override suspend fun load(params: LoadParams<Int>): PagingSource.LoadResult<Int, Complaints> {
        return try {
            Log.d(TAG, "load: %%%%%%%%%%%%%%%%%%%")
            val page = params.key ?: 0
//            var response : BaseResponse<PagingResult<ComplaintsResponse>>? = null
//            when(flag){
//                0 -> response = complaintsApi.selectAllComplaints(page, 10)
//                1 -> response = complaintsApi.selectComplaintsByAngel(page, 10)
//                2 -> response = complaintsApi.selectComplaintsByBlind(page, 10)
//            }
            val response = complaintsApi.selectAllComplaints(page, 1)
            Log.d(TAG, "load: totalPage ${response!!.data.totalPage}")
            Log.d(TAG, "load: list ${response!!.data.result}")

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
