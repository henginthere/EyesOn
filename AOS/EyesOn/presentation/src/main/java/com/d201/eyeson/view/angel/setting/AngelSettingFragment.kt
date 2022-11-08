package com.d201.eyeson.view.angel.setting

import androidx.navigation.fragment.findNavController
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentAngelSettingBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "AngelSettingFragment"
@AndroidEntryPoint
class AngelSettingFragment : BaseFragment<FragmentAngelSettingBinding>(R.layout.fragment_angel_setting) {

    override fun init() {
        initView()
        initViewModel()
    }

    private fun initView() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnLogout.setOnClickListener {

            }
            btnResign.setOnClickListener {

            }
            switchAlarm.setOnCheckedChangeListener { compoundButton, b ->

            }
        }
    }

    private fun initViewModel() {
        TODO("Not yet implemented")
    }

}