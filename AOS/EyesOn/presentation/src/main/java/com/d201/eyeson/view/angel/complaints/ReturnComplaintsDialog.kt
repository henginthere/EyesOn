package com.d201.eyeson.view.angel.complaints

import com.d201.domain.model.Complaints
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseDialogFragment
import com.d201.eyeson.databinding.DialogReturnComplaintsBinding
import com.d201.eyeson.view.angel.ReturnConfirmListener
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "ReturnComplaintsDialog"

@AndroidEntryPoint
class ReturnComplaintsDialog(
    private var complaints: Complaints,
    private val returnConfirmListener: ReturnConfirmListener
) : BaseDialogFragment<DialogReturnComplaintsBinding>(R.layout.dialog_return_complaints) {
    override fun init() {
        initView()
    }

    private fun initView() {
        binding.apply {
            btnConfirm.setOnClickListener {
                if (etReturn.text.isNotBlank()) {
                    complaints.returnContent = etReturn.text.toString()
                    returnConfirmListener.onClick(complaints)
                    dismiss()
                } else {
                    showToast("반환 사유를 입력해주세요")
                }
            }
            btnCancel.setOnClickListener {
                dismiss()
            }
        }
    }

}