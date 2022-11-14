package com.d201.data.datasource.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.d201.data.datasource.ComplaintsRemoteDataSource
import com.d201.data.mapper.mapperToListComplaints
import com.d201.data.model.response.ComplaintsResponse
import com.d201.domain.base.BaseResponse
import com.d201.domain.model.Complaints
import com.d201.domain.model.PagingResult
import kotlinx.coroutines.flow.collectLatest

private const val TAG ="ComplaintsPagingSource"
class ComplaintsPagingSource(private val complaintsRemoteDataSource: ComplaintsRemoteDataSource, private val flag: Int): PagingSource<Int, Complaints>() {

    override suspend fun load(params: LoadParams<Int>): PagingSource.LoadResult<Int, Complaints> {
        return try {
            val page = params.key ?: 0
            var response : BaseResponse<PagingResult<ComplaintsResponse>>? = null
            complaintsRemoteDataSource.selectComplaintsList(flag, page, size = 15).collectLatest {
                response = it
            }
            if(response!!.data.totalPage > 0){
                PagingSource.LoadResult.Page(
                    data = response!!.data.result.mapperToListComplaints(),
                    prevKey = if(page <= 0) null else page - 1,
                    nextKey = if(page == response!!.data.totalPage || response!!.data.totalPage == 0) null else page + 1
                )
            }else{
                LoadResult.Page(
                    data = response!!.data.result.mapperToListComplaints(),
                    prevKey = if(page == 0) null else page -1,
                    nextKey = null
                )
            }
        }catch (e: Exception){
            Log.d(TAG, "load: ${e.message}")
            PagingSource.LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Complaints>): Int? {
        TODO("Not yet implemented")
    }
}
