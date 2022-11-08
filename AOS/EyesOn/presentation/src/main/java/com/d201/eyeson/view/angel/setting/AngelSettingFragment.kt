package com.d201.eyeson.view.angel.setting

import android.content.Intent
import android.widget.CompoundButton
import android.widget.ToggleButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentAngelSettingBinding
import com.d201.eyeson.view.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File

private const val TAG = "AngelSettingFragment"

@AndroidEntryPoint
class AngelSettingFragment :
    BaseFragment<FragmentAngelSettingBinding>(R.layout.fragment_angel_setting) {

    private val angelSettingViewModel : AngelSettingViewModel by viewModels()

    private var toggleDays = listOf(binding.toggleSun, binding.toggleMon, binding.toggleTue, binding.toggleWed, binding.toggleThu, binding.toggleFri, binding.toggleSat)

    override fun init() {
        initView()
        initViewModelCallback()
    }

    private fun initView() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }

            switchAlarm.setOnCheckedChangeListener { compoundButton, b ->

            }
            btnLogout.setOnClickListener {
                clearApplicationData()
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
                requireActivity().finish()
            }
            btnResign.setOnClickListener {

            }
        }
    }

    private fun initViewModelCallback() {
        lifecycleScope.launch {
            angelSettingViewModel.angelInfo.collectLatest {
                val alarmDay = it!!.alarmDay

                for (i in 0 .. 6){
                    if(0 != (alarmDay and (64 shr i))){
                        toggleDays[i].isChecked = true
                    }
                }
            }
        }
    }

    // 데이터 삭제
    fun clearApplicationData() {
        val cache: File = requireActivity().cacheDir
        val appDir = File(cache.parent)
        if (appDir.exists()) {
            val children = appDir.list()
            for (s in children!!) {
                if (s != "lib" && s != "shared_prefs") {
                    deleteDir(File(appDir, s))
                }
            }
        }
    }

    fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children: Array<String> = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        // The directory is now empty or this is a file so delete it
        return dir!!.delete()
    }


}