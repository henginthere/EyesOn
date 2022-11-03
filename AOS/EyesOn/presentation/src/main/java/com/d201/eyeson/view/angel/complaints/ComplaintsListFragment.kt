package com.d201.eyeson.view.angel.complaints

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentComplaintsListBinding
import com.d201.eyeson.view.angel.ComplaintsClickListener
import com.d201.eyeson.view.angel.main.AngelMainFragmentDirections
import com.d201.eyeson.view.angel.main.AngelMainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "ComplaintsListFragment"
@AndroidEntryPoint
class ComplaintsListFragment : BaseFragment<FragmentComplaintsListBinding>(R.layout.fragment_complaints_list) {

    private val angelMainViewModel: AngelMainViewModel by viewModels()
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
        }
    }

    private fun initViewModel() {
        job = lifecycleScope.launch{
            angelMainViewModel.getComplaintsList(0).collectLatest {
                complaintsAdapter.submitData(it)
            }
        }

    }

    private val complaintsClickListener = object : ComplaintsClickListener {
        override fun onClick(complaintsSeq: Long) {
            findNavController().navigate(ComplaintsListFragmentDirections.actionComplaintsListFragmentToComplaintsDetailFragment(complaintsSeq))
        }
    }
}
