package com.d201.eyeson.view.login.join

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentSelectGenderBinding
import com.d201.eyeson.util.GENDER_FEMALE
import com.d201.eyeson.util.GENDER_MALE
import com.d201.eyeson.view.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "SelectGenderFragment"
@AndroidEntryPoint
class SelectGenderFragment : BaseFragment<FragmentSelectGenderBinding>(R.layout.fragment_select_gender) {

    private val viewModel: SelectGenderViewModel by viewModels()
    private val args: SelectGenderFragmentArgs by navArgs()
    override fun init() {
        if(args.role.isEmpty()){
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
        initView()
        initViewModel()
    }

    private fun initView(){
        binding.apply {
            btnMale.setOnClickListener {
                viewModel.putUserRole(args.role, GENDER_MALE)
            }
            btnFemale.setOnClickListener {
                viewModel.putUserRole(args.role, GENDER_FEMALE)
            }
        }
    }

    private fun initViewModel(){
        lifecycleScope.launch {
            viewModel.login.collectLatest {
                if(it != null) {
                    findNavController().navigate(SelectGenderFragmentDirections.actionSelectGenderFragmentToJoinSuccessFragment())
                }
            }
        }
    }

}