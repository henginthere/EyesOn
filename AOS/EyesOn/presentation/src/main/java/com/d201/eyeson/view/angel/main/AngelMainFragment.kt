package com.d201.eyeson.view.angel.main

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.d201.data.utils.SELELCT_ALL
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentAngelMainBinding
import com.d201.eyeson.view.angel.ComplaintsClickListener
import com.d201.eyeson.view.angel.help.AngelHelpActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "AngelMainFragment"
@AndroidEntryPoint
class AngelMainFragment : BaseFragment<FragmentAngelMainBinding>(R.layout.fragment_angel_main) {

    private val angelMainViewModel: AngelMainViewModel by viewModels()
    private lateinit var job: Job
    private lateinit var angelMainAdapter: AngelMainAdapter

    override fun init() {
        initListener()
        initView()
        initViewModelCallback()
        angelMainViewModel.getAngelInfo()
        actionCheck()
    }

    private fun actionCheck() {
        val bundle = requireActivity().intent.extras
        var action = ""

        if(bundle != null && bundle.containsKey("action")){
            action = bundle.getString("action")!!
        }

        if(action.isNotEmpty() && action == "AngelHelp"){
            startActivity(Intent(requireActivity(), AngelHelpActivity::class.java))
        }
    }

    private fun initListener() {
        binding.apply {
            btnSetting.setOnClickListener {
                findNavController().navigate(AngelMainFragmentDirections.actionAngelMainFragmentToAngelSettingFragment())
            }
        }
    }

    private fun initView() {
        angelMainAdapter = AngelMainAdapter(complaintsClickListener)
        binding.apply {
            vm = angelMainViewModel
            rvComplaintsList.apply {
                adapter = angelMainAdapter
            }
            tvTitleComplaints.setOnClickListener { findNavController().navigate(AngelMainFragmentDirections.actionAngelMainFragmentToComplaintsListFragment()) }
            cvComplaintsList.setOnClickListener {  }
            cvResponseHelp.setOnClickListener {  }
        }
    }

    private fun initViewModelCallback(){
        job = lifecycleScope.launch { 
            angelMainViewModel.getComplaintsList().collectLatest {
                angelMainAdapter.submitData(it)
            }
        }
        lifecycleScope.launch{
            angelMainViewModel.apply {
                angelInfoData.collectLatest {
                }
            }
        }
        

    }

    private val complaintsClickListener = object : ComplaintsClickListener {
        override fun onClick(complaintsSeq: Long) {
            findNavController().navigate(AngelMainFragmentDirections.actionAngelMainFragmentToComplaintsDetailFragment(complaintsSeq))
        }
    }

}