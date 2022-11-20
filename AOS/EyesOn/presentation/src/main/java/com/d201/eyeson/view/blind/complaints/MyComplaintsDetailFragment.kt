package com.d201.eyeson.view.blind.complaints

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d201.domain.model.Complaints
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentMyComplaintsDetailBinding
import com.d201.eyeson.util.accessibilityEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "MyComplaintsDetailFragment"

@AndroidEntryPoint
class MyComplaintsDetailFragment :
    BaseFragment<FragmentMyComplaintsDetailBinding>(R.layout.fragment_my_complaints_detail) {
    private val args: MyComplaintsDetailFragmentArgs by navArgs()
    private val viewModel: MyComplaintsViewModel by viewModels()
    private lateinit var complaints: Complaints
    override fun init() {
        initViewModel()
        initView()
    }

    private fun initViewModel() {
        viewModel.apply {
            getComplaints(args.complaintSeq)
        }
        lifecycleScope.launch {
            viewModel.complaints.collectLatest {
                if (it != null) {
                    complaints = it
                }
            }
        }
    }

    private fun initView() {
        binding.apply {
            vm = viewModel
            btnBack.apply {
                accessibilityDelegate = accessibilityEvent(this, requireContext())
                setOnClickListener {
                    findNavController().popBackStack()
                }
            }
        }
    }
}
