package com.d201.eyeson.view.angel.complaints

import android.content.Intent
import android.net.Uri
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentComplaintsDetailBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "ComplaintsDetailFragment"
@AndroidEntryPoint
class ComplaintsDetailFragment : BaseFragment<FragmentComplaintsDetailBinding>(R.layout.fragment_complaints_detail) {

    override fun init() {
        initView()
    }

    private fun initView() {
        binding.apply {
            btnGoSafetyEReport.setOnClickListener {
                openWebPage()
            }
        }
    }

    private fun openWebPage(){
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.safetyreport.go.kr/#safereport/safereport")))
    }
}