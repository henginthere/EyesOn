package com.d201.eyeson.view.blind.help

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentSelectHelperBinding
import com.d201.eyeson.util.GENDER_DEFAULT
import com.d201.eyeson.util.accessibilityEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "SelectHelperFragment"
@AndroidEntryPoint
class SelectHelperFragment :
    BaseFragment<FragmentSelectHelperBinding>(R.layout.fragment_select_helper) {

    private val blindHelpViewModel: BlindHelpViewModel by activityViewModels()

    override fun init() {
        initListener()
        initViewModelCallback()
    }

    private fun initListener() {
        binding.apply {
            btnSameGender.setOnClickListener {
                val gender = requireActivity().intent.getStringExtra("Gender")!!
                blindHelpViewModel.requestHelp(gender)
            }
            btnAnyone.setOnClickListener {
                blindHelpViewModel.requestHelp(GENDER_DEFAULT)
            }
            btnBack.apply {
                accessibilityDelegate = accessibilityEvent(this, requireContext())
                setOnClickListener {
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun initViewModelCallback() {
        lifecycleScope.launch {
            blindHelpViewModel.sessionId.collectLatest { sessionId ->
                when (sessionId) {
                    -1 -> showToast("현재 도움 받을 수 있는 엔젤이 없어요")
                    else -> findNavController().navigate(SelectHelperFragmentDirections.actionSelectHelperFragmentToBlindHelpFragment())
                }
            }
        }
    }
}