package com.d201.eyeson.view.blind

import com.d201.domain.model.Complaints
import com.d201.domain.model.Noti

interface BlindComplaintsClickListener {
    fun onClick(complaints: Complaints)
}

interface NotiClickListener {
    fun onClick(noti: Noti, position: Int)
}

interface BlindHelpDisconnectListener {
    fun onClick()
}