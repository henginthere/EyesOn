package com.d201.domain.model

import com.google.gson.annotations.SerializedName

data class PagingResult<T>(
    val page: Int,
    val totalPage: Int,
    val result: List<T>
)