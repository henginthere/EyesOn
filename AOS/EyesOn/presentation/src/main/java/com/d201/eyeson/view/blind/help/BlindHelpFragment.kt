package com.d201.eyeson.view.blind.help

import androidx.navigation.fragment.navArgs
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentBlindHelpBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "BlindHelpFragment"
@AndroidEntryPoint
class BlindHelpFragment : BaseFragment<FragmentBlindHelpBinding>(R.layout.fragment_blind_help) {

    private val args : BlindHelpFragmentArgs by navArgs()

    override fun init() {
        initView()
    }

    private fun initView() {
        binding.apply {
            tvSessionId.text = args.sessionId.toString()
        }
    }

}