package com.d201.eyeson.view.login.login

import com.d201.eyeson.R
import com.d201.eyeson.base.BaseDialogFragment
import com.d201.eyeson.databinding.DialogPermissionBinding

class PermissionDialog : BaseDialogFragment<DialogPermissionBinding>(R.layout.dialog_permission) {
    override fun init() {
        initView()
    }

    private fun initView() {
        binding.apply {
            btnConfirm.setOnClickListener{
                dismiss()
            }
        }
    }
}
