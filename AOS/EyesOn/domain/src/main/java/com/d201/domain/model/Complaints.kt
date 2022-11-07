package com.d201.domain.model

data class Complaints(
    val seq: Long,
    val address: String?,
    val content: String?,
    val image: String?,
    val regTime: String?,
    val resultContent: String?,
    var returnContent: String?,
    val state: String?,
    val title: String?
){
    constructor(address: String?, content: String?): this(seq = 0L, address, content, image = null, regTime = null, resultContent = null, returnContent = null, state = null, title = null)

}