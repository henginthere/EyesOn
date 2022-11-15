package com.d201.eyeson.view.angel.main

import android.Manifest
import android.content.Intent
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentAngelMainBinding
import com.d201.eyeson.util.*
import com.d201.eyeson.view.angel.ComplaintsClickListener
import com.d201.eyeson.view.angel.help.AngelHelpActivity
import com.d201.eyeson.view.blind.BlindMainFragmentDirections
import com.d201.eyeson.view.blind.findobject.FindObjectActivity
import com.d201.eyeson.view.blind.help.BlindHelpActivity
import com.d201.eyeson.view.blind.scanobstacle.ScanObstacleActivity
import com.d201.eyeson.view.blind.scantext.ScanTextActivity
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "AngelMainFragment"

@AndroidEntryPoint
class AngelMainFragment : BaseFragment<FragmentAngelMainBinding>(R.layout.fragment_angel_main) {

    private val angelMainViewModel: AngelMainViewModel by viewModels()
    private lateinit var job: Job
    private lateinit var angelMainAdapter: AngelMainAdapter

    override fun init() {
        initListener()
        initView()
        initViewModelCallback()
        angelMainViewModel.getAngelInfo()
        actionCheck()
    }

    override fun onResume() {
        super.onResume()
        angelMainViewModel.getAngelInfo()
    }

    private fun actionCheck() {
        val bundle = requireActivity().intent.extras
        var action = ""

        if (bundle != null && bundle.containsKey("action")) {
            action = bundle.getString("action")!!
        }

        if (action.isNotEmpty() && action == "AngelHelp") {
            Log.d(TAG, "actionCheck: $action")
            requireActivity().intent.putExtra("action", "")
            checkPermission(VIEW_ANGEL_HELP, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS)
        }
    }

    private fun initListener() {
        binding.apply {
            btnSetting.setOnClickListener {
                findNavController().navigate(AngelMainFragmentDirections.actionAngelMainFragmentToAngelSettingFragment())
            }
        }
    }

    private fun initView() {
        angelMainAdapter = AngelMainAdapter(complaintsClickListener)
        binding.apply {
            vm = angelMainViewModel
            rvComplaintsList.apply {
                adapter = angelMainAdapter
            }
            tvTitleComplaints.setOnClickListener {
                findNavController().navigate(
                    AngelMainFragmentDirections.actionAngelMainFragmentToComplaintsListFragment()
                )
            }
            cvComplaintsList.setOnClickListener { }
            cvResponseHelp.setOnClickListener { }
        }
    }

    private fun initViewModelCallback(){
        job = lifecycleScope.launch { 
            angelMainViewModel.complaintsList.collectLatest {
                if(it != null) {
                    angelMainAdapter.submitData(it)
                }
            }
        }
        lifecycleScope.launch {
            angelMainViewModel.apply {
                angelInfoData.collectLatest {
                }
            }
        }
        angelMainViewModel.getComplaintsList()

    }

    private val complaintsClickListener = object : ComplaintsClickListener {
        override fun onClick(complaintsSeq: Long) {
            findNavController().navigate(
                AngelMainFragmentDirections.actionAngelMainFragmentToComplaintsDetailFragment(
                    complaintsSeq
                )
            )
        }
    }

    private fun checkPermission(direction: Int, vararg permissions: String) {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                when (direction) {
                    VIEW_ANGEL_HELP -> startActivity(
                        Intent(
                            requireActivity(),
                            AngelHelpActivity::class.java
                        )
                    )
                }
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                showToast("권한을 허용해야 이용이 가능합니다.")
            }

        }
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setDeniedMessage("권한을 허용해주세요. [설정] > [앱 및 알림] > [고급] > [앱 권한]")
            .setPermissions(
                *permissions
            )
            .check()
    }

}