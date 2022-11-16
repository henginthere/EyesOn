package com.d201.eyeson.view.blind.guide

import com.d201.eyeson.R
import com.d201.eyeson.base.BaseDialogFragment
import com.d201.eyeson.databinding.DialogGuideBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "GuideDialog"

@AndroidEntryPoint
class GuideDialog : BaseDialogFragment<DialogGuideBinding>(R.layout.dialog_guide) {

    override fun init() {
    }

}