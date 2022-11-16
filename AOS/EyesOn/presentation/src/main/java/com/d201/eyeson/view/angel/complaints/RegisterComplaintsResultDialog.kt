package com.d201.eyeson.view.angel.complaints

import com.d201.domain.model.Complaints
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseDialogFragment
import com.d201.eyeson.databinding.DialogRegisterComplaintsResultBinding
import com.d201.eyeson.view.angel.RegisterComplaintsListener

private const val TAG = "ComplaintsDoneDialog"

class RegisterComplaintsResultDialog(
    private val complaints: Complaints,
    private val registerComplaintsListener: RegisterComplaintsListener
) : BaseDialogFragment<DialogRegisterComplaintsResultBinding>(R.layout.dialog_register_complaints_result) {
    override fun init() {
        initListener()
    }

    private fun initListener() {
        binding.apply {
            btnConfirm.setOnClickListener {
                if (etResult.text.isNotBlank()) {
                    complaints.title = etResult.text.toString()
                    registerComplaintsListener.onClick(complaints)
                    dismiss()
                } else {
                    showToast("민원 제목을 입력해주세요")
                }
            }
            btnCancel.setOnClickListener {
                dismiss()
            }
        }
    }
}
