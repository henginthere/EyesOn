package com.d201.eyeson.view.angel.complaints

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentComplaintsListBinding
import com.d201.eyeson.view.angel.ComplaintsClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "ComplaintsListFragment"

@AndroidEntryPoint
class ComplaintsListFragment :
    BaseFragment<FragmentComplaintsListBinding>(R.layout.fragment_complaints_list) {

    private val complaintsListviewModel: ComplaintsListViewModel by activityViewModels()
    private lateinit var complaintsAdapter: ComplaintsAdapter

    override fun init() {
        initView()
        initListener()
        initViewModelCallback()
        getComplaintsList()
    }

    private fun initView() {
        val complaintsClickListener = object : ComplaintsClickListener {
            override fun onClick(complaintsSeq: Long) {
                findNavController().navigate(
                    ComplaintsListFragmentDirections.actionComplaintsListFragmentToComplaintsDetailFragment(
                        complaintsSeq
                    )
                )
            }
        }

        complaintsAdapter = ComplaintsAdapter(complaintsClickListener)
        binding.apply {
            rvComplaintsList.apply {
                adapter = complaintsAdapter

            }
        }
    }

    private fun initListener() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initViewModelCallback() {
        lifecycleScope.launch {
            complaintsListviewModel.complaintsList.collectLatest {
                if (it != null) {
                    complaintsAdapter.submitData(it)
                }
            }
        }

    }

    private fun getComplaintsList() {
        complaintsListviewModel.getComplaintsList()
    }

}
