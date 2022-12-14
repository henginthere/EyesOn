package com.d201.domain.model

data class PagingResult<T>(
    val page: Int,
    val totalPage: Int,
    val result: List<T>
)