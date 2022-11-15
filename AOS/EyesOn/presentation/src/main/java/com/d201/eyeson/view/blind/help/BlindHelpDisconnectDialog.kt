package com.d201.eyeson.view.blind.help

import com.d201.eyeson.R
import com.d201.eyeson.base.BaseDialogFragment
import com.d201.eyeson.databinding.DialogHelpDisconnectBinding
import com.d201.eyeson.view.angel.AngelHelpDisconnectListener
import com.d201.eyeson.view.blind.BlindHelpDisconnectListener

class BlindHelpDisconnectDialog(private val blindHelpDisconnectListener: BlindHelpDisconnectListener) : BaseDialogFragment<DialogHelpDisconnectBinding>(R.layout.dialog_help_disconnect) {

    override fun init() {
        isCancelable = false
        initListener()
    }

    private fun initListener(){
        binding.btnConfirm.setOnClickListener {
            blindHelpDisconnectListener.onClick()
            dismiss()
        }
    }

}