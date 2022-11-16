package com.d201.domain.model

data class Complaints(
    val seq: Long,
    val address: String?,
    val content: String?,
    val image: String?,
    val regTime: String?,
    val resultContent: String?,
    var returnContent: String?,
    var state: String?,
    var title: String?
) {
    constructor(address: String?, content: String?) : this(
        seq = 0L,
        address,
        content,
        image = "",
        regTime = "",
        resultContent = "",
        returnContent = "",
        state = "",
        title = ""
    )

}