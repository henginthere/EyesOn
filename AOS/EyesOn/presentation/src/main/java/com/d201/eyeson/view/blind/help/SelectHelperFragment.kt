package com.d201.eyeson.view.blind.help

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentSelectHelperBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "SelectHelperFragment"
@AndroidEntryPoint
class SelectHelperFragment : BaseFragment<FragmentSelectHelperBinding>(R.layout.fragment_select_helper) {

    private val blindHelpViewModel : BlindHelpViewModel by activityViewModels()

    override fun init() {
        initListener()
        initViewModelCallback()
    }

    private fun initListener(){
        binding.apply {
            btnSameGender.setOnClickListener {
                val gender = requireActivity().intent.getStringExtra("Gender")!!
                blindHelpViewModel.requestHelp(gender)
            }
            btnAnyone.setOnClickListener {
                blindHelpViewModel.requestHelp("d")
            }
        }
    }

    private fun initViewModelCallback() {
        lifecycleScope.launch{
            blindHelpViewModel.sessionId.collectLatest {
                if(it != -1){
                    findNavController().navigate(SelectHelperFragmentDirections.actionSelectHelperFragmentToBlindHelpFragment())
                }
            }
        }
    }
}