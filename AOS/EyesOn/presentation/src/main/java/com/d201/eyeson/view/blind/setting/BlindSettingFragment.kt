package com.d201.eyeson.view.blind.setting

import android.content.SharedPreferences
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentBlindSettingBinding
import com.d201.eyeson.util.JWT
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
                sharedPref.edit().remove(JWT)
            }

            btnSignOut.setOnClickListener {

            }
        }
    }
}