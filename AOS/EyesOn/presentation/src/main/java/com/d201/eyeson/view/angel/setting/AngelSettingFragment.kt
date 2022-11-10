package com.d201.eyeson.view.angel.setting

import android.content.Intent
import android.widget.ToggleButton
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentAngelSettingBinding
import com.d201.eyeson.view.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "AngelSettingFragment"
@AndroidEntryPoint
class AngelSettingFragment : BaseFragment<FragmentAngelSettingBinding>(R.layout.fragment_angel_setting) {

    private lateinit var buttonList : List<ToggleButton>
    private val viewModel: AngelSettingViewModel by viewModels()
    override fun init() {
        initView()
        initViewModel()
    }

    private fun initView() {
        binding.apply {
            buttonList = listOf(toggleSun, toggleMon, toggleTue, toggleWed, toggleThu, toggleFri, toggleSat)
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnLogout.setOnClickListener {
                finishActivity()
            }
            btnResign.setOnClickListener {
                viewModel.deleteUser()

            }
            switchAlarm.setOnCheckedChangeListener { compoundButton, b ->

            }
        }
    }

    private fun initViewModel() {
        viewModel.apply {
            deleteUserEvent.observe(viewLifecycleOwner){
                if(it){
                    finishActivity()
                }
            }
        }
    }

    private fun finishActivity(){
        requireActivity().startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }

}