package com.d201.eyeson.view.blind.help

import com.d201.eyeson.R
import com.d201.eyeson.base.BaseDialogFragment
import com.d201.eyeson.databinding.DialogHelpDisconnectBinding
import com.d201.eyeson.view.blind.BlindHelpDisconnectListener

private const val TAG = "BlindHelpDisconnectDialog"

class BlindHelpDisconnectDialog(private val blindHelpDisconnectListener: BlindHelpDisconnectListener) :
    BaseDialogFragment<DialogHelpDisconnectBinding>(R.layout.dialog_help_disconnect) {

    override fun init() {
        initListener()
    }

    private fun initListener() {
        binding.btnConfirm.setOnClickListener {
            blindHelpDisconnectListener.onClick()
            dismiss()
        }
    }

}