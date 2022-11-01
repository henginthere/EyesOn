package com.d201.eyeson.view.blind

import androidx.navigation.fragment.findNavController
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentBlindMainBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "BlindMainFragment"
@AndroidEntryPoint
class BlindMainFragment : BaseFragment<FragmentBlindMainBinding>(R.layout.fragment_blind_main) {

    override fun init() {
        initListener()
    }

    private fun initListener() {
        binding.apply {
            btnScanText.setOnClickListener {

            }
        }
    }


}