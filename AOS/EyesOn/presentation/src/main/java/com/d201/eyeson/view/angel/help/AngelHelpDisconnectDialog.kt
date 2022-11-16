package com.d201.eyeson.view.angel.help

import com.d201.eyeson.R
import com.d201.eyeson.base.BaseDialogFragment
import com.d201.eyeson.databinding.DialogHelpDisconnectBinding
import com.d201.eyeson.view.angel.AngelHelpDisconnectListener

class AngelHelpDisconnectDialog(private val angelHelpDisconnectListener: AngelHelpDisconnectListener) :
    BaseDialogFragment<DialogHelpDisconnectBinding>(R.layout.dialog_help_disconnect) {

    override fun init() {
        initListener()
    }

    private fun initListener() {
        binding.btnConfirm.setOnClickListener {
            angelHelpDisconnectListener.onClick()
            dismiss()
        }
    }

}