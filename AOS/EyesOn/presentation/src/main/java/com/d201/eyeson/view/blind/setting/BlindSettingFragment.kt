package com.d201.eyeson.view.blind.setting

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentBlindSettingBinding
import com.d201.eyeson.util.JWT
import com.d201.eyeson.view.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val TAG = "BlindSettingFragment"
@AndroidEntryPoint
class BlindSettingFragment : BaseFragment<FragmentBlindSettingBinding>(R.layout.fragment_blind_setting) {
    private lateinit var sharedPref: SharedPreferences
    override fun init() {
        initListener()
    }
    private fun initListener() {
        binding.apply {

            btnBack.setOnClickListener{  }

            btnReplayGuide.setOnClickListener {  }

            btnLogout.setOnClickListener {
                showDialog()
            }

            btnSignOut.setOnClickListener {

            }
        }
    }
    private fun showDialog(){
        val builder = AlertDialog.Builder(requireContext())
        builder
            .setTitle("로그아웃 할까요?")
            .setIcon(R.drawable.ic_launcher_foreground)
            .setPositiveButton("예") { dialog , which ->
                sharedPref.edit().remove(JWT)
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            }
            .setNegativeButton("취소"){ dialog, which ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
    
}