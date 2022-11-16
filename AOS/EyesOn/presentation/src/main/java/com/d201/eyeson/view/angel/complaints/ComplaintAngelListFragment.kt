package com.d201.eyeson.view.angel.complaints

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentComplaintAngelListBinding
import com.d201.eyeson.databinding.FragmentComplaintsListBinding
import com.d201.eyeson.view.angel.ComplaintsClickListener
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "ComplaintAngelListFragment"
class ComplaintAngelListFragment :
    BaseFragment<FragmentComplaintAngelListBinding>(R.layout.fragment_complaint_angel_list) {
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
                    ComplaintAngelListFragmentDirections.actionComplaintAngelListFragmentToComplaintsDetailFragment(complaintsSeq)
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
            complaintsListviewModel.complaintsMyList.collectLatest {
                if (it != null) {
                    complaintsAdapter.submitData(it)
                }
            }
        }

    }
    private fun getComplaintsList() {
        complaintsListviewModel.getComplaintsByAngelList()
    }
}