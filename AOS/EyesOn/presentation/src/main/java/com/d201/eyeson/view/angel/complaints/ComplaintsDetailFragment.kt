package com.d201.eyeson.view.angel.complaints

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d201.domain.model.Complaints
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentComplaintsDetailBinding
import com.d201.eyeson.view.angel.ReturnConfirmListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "ComplaintsDetailFragment"
@AndroidEntryPoint
class ComplaintsDetailFragment : BaseFragment<FragmentComplaintsDetailBinding>(R.layout.fragment_complaints_detail) {

    private val complaintsViewModel: ComplaintsViewModel by viewModels()
    private val args: ComplaintsDetailFragmentArgs by navArgs()

    override fun init() {
        initView()
        initViewModel()
    }

    private fun initViewModel() {
        complaintsViewModel.apply {
            getComplaints(args.complaintsSeq)
        }

    }

    private fun initView() {
        binding.apply {
            vm = complaintsViewModel

            btnGoSafetyEReport.setOnClickListener {
                openWebPage()
            }
            btnReject.setOnClickListener {
                ReturnComplaintsDialog(complaintsViewModel.complaints.value!!, returnConfirmListener).let {
                    it.show(parentFragmentManager, "ReturnComplaints")
                }
            }
        }
    }

    private fun openWebPage(){
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.safetyreport.go.kr/#safereport/safereport")))
    }

    private val returnConfirmListener = object : ReturnConfirmListener{
        override fun onClick(complaints: Complaints) {
            complaintsViewModel.returnComplaints(complaints)
            findNavController().popBackStack()
        }
    }
}