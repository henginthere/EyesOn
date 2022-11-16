package com.d201.eyeson.view.blind.complaints

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.d201.domain.model.Complaints
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentMyComplaintsBinding
import com.d201.eyeson.view.blind.BlindComplaintsClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "MyComplaintsFragment"

@AndroidEntryPoint
class MyComplaintsFragment :
    BaseFragment<FragmentMyComplaintsBinding>(R.layout.fragment_my_complaints) {

    private val viewModel: MyComplaintsViewModel by viewModels()
    private lateinit var job: Job
    private lateinit var myComplaintsAdapter: MyComplaintsAdapter

    override fun init() {

        initView()
        initViewModel()
    }

    private fun initView() {
        myComplaintsAdapter = MyComplaintsAdapter(blindComplaintsClickListener)
        binding.apply {
            rvMyComplaints.apply {
                adapter = myComplaintsAdapter
            }
        }
    }

    private fun initViewModel() {
        job = lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getComplaintsList().collectLatest {
                myComplaintsAdapter.submitData(it)
            }
        }
    }

    private val blindComplaintsClickListener = object : BlindComplaintsClickListener {
        override fun onClick(complaints: Complaints) {
            findNavController().navigate(
                MyComplaintsFragmentDirections.actionMyComplaintsFragmentToMyComplaintsDetailFragment(
                    complaints.seq
                )
            )
        }
    }
}