package com.d201.eyeson.view.angel

import com.d201.domain.model.Complaints

interface ComplaintsClickListener{
    fun onClick(complaintsSeq: Long)
}

interface ReturnConfirmListener{
    fun onClick(complaints: Complaints)
}