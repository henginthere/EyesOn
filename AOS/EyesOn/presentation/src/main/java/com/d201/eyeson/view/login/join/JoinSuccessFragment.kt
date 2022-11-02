package com.d201.eyeson.view.login.join

import androidx.fragment.app.viewModels
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentJoinSuccessBinding
import com.d201.eyeson.view.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "JoinSuccessFragment"
@AndroidEntryPoint
class JoinSuccessFragment :
    BaseFragment<FragmentJoinSuccessBinding>(R.layout.fragment_join_success) {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun init() {
        initView()
        initListener()
    }

    private fun initView() {
        binding.apply {
        }
    }

    private fun initListener() {
        binding.apply {
            btnContinue.setOnClickListener {

            }
        }
    }

}