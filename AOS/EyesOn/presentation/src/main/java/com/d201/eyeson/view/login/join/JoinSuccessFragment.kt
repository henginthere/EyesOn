package com.d201.eyeson.view.login.join

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentJoinSuccessBinding
import com.d201.eyeson.util.*
import com.d201.eyeson.view.angel.main.AngelMainActivity
import com.d201.eyeson.view.blind.BlindMainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "JoinSuccessFragment"
@AndroidEntryPoint
class JoinSuccessFragment : BaseFragment<FragmentJoinSuccessBinding>(R.layout.fragment_join_success) {

    private val args: JoinSuccessFragmentArgs by navArgs()

    override fun init() {
        initView()
        initListener()
        lifecycleScope.launch(Dispatchers.IO){
            delay(3500)
            launchMainView()
        }
    }

    private fun initView() {
        binding.apply {
            tvSelectedRole.text = when(args.role){
                ANGEL -> "엔젤"
                BLIND -> "시각장애인"
                else -> "?"
            }
            tvSelectedGender.text = when(args.gender){
                GENDER_MALE -> "남성"
                GENDER_FEMALE -> "여성"
                GENDER_DEFAULT -> "설정 안됨"
                else -> "?"
            }
            animationOk.playAnimation()
        }
    }

    private fun initListener() {
        binding.apply {
        }
    }

    private fun launchMainView(){
        when(args.role){
            BLIND -> startActivity(Intent(requireContext(), BlindMainActivity::class.java).apply {
                putExtra("Gender", args.gender)
            })
            ANGEL -> startActivity(Intent(requireContext(), AngelMainActivity::class.java).apply {
                putExtra("Gender", args.gender)
            })
            else -> return
        }
        requireActivity().finish()
    }

}