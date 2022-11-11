package com.d201.eyeson.view.angel.complaints

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.d201.domain.model.Complaints
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseDialogFragment
import com.d201.eyeson.databinding.DialogRegisterTitleBinding
import com.d201.eyeson.view.angel.TitleConfirmListener

class RegisterTitleDialog(private val complaints: Complaints, private val titleConfirmListener: TitleConfirmListener) : BaseDialogFragment<DialogRegisterTitleBinding>(R.layout.dialog_register_title) {
    override fun init() {
        initView()

    }

    private fun initView() {
        binding.apply {
            btnConfirm.setOnClickListener {
                if(etTitle.text.isNotBlank()) {
                    complaints.title = etTitle.text.toString()
                    titleConfirmListener.onClick(complaints)
                    dismiss()
                }else{
                    showToast("민원 제목을 입력해주세요")
                }
            }
            btnCancel.setOnClickListener {
                dismiss()
            }
        }
    }

}
