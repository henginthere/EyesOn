package com.d201.eyeson.view.angel

import com.d201.domain.model.Complaints

interface ComplaintsClickListener {
    fun onClick(complaintsSeq: Long)
}

interface ReturnConfirmListener {
    fun onClick(complaints: Complaints)
}

interface TitleConfirmListener {
    fun onClick(complaints: Complaints)
}

interface RegisterComplaintsListener {
    fun onClick(complaints: Complaints)
}

interface ComplaintsImageClickListener {
    fun onClick(image: String)
}

interface AngelHelpDisconnectListener {
    fun onClick()
}