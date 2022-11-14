package com.d201.eyeson.view.angel.complaints

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentComplaintsListBinding
import com.d201.eyeson.view.angel.ComplaintsClickListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "ComplaintsListFragment"
@AndroidEntryPoint
class ComplaintsListFragment : BaseFragment<FragmentComplaintsListBinding>(R.layout.fragment_complaints_list) {

    private val viewModel: ComplaintsListViewModel by viewModels()
    private lateinit var job: Job
    private lateinit var complaintsAdapter: ComplaintsAdapter

    override fun init() {
        initView()
        initViewModel()
    }

    private fun initView() {
        complaintsAdapter = ComplaintsAdapter(complaintsClickListener)
        binding.apply {
            rvComplaintsList.apply {
                adapter = complaintsAdapter
            }
            tabAngelComplaintsList.addOnTabSelectedListener(object : OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab!!.position){
                        0 -> viewModel.getComplaintsList()
                        1 -> viewModel.getComplaintsByAngelList()

                    }
                }
                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {
                    when(tab!!.position){
                        0 -> viewModel.getComplaintsList()
                        1 -> viewModel.getComplaintsByAngelList()
                    }
                }
            })
        }
    }

    private fun initViewModel() {
        job = lifecycleScope.launch{
            viewModel.complaintsList.collectLatest {
                if(it != null) {
                    complaintsAdapter.submitData(it)
                }
            }
        }
        viewModel.getComplaintsList()


    }

    private val complaintsClickListener = object : ComplaintsClickListener {
        override fun onClick(complaintsSeq: Long) {
            findNavController().navigate(ComplaintsListFragmentDirections.actionComplaintsListFragmentToComplaintsDetailFragment(complaintsSeq))
        }
    }
}
