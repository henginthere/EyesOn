package com.d201.eyeson.view.blind

import android.Manifest
import android.content.Intent
import androidx.navigation.fragment.findNavController
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentBlindMainBinding
import com.d201.eyeson.util.*
import com.d201.eyeson.view.blind.findobject.FindObjectActivity
import com.d201.eyeson.view.blind.help.BlindHelpActivity
import com.d201.eyeson.view.blind.scanobstacle.ScanObstacleActivity
import com.d201.eyeson.view.blind.scantext.ScanTextActivity
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "BlindMainFragment"
@AndroidEntryPoint
class BlindMainFragment : BaseFragment<FragmentBlindMainBinding>(R.layout.fragment_blind_main) {

    override fun init() {
        initListener()
    }

    private fun initListener() {
        binding.apply {
            btnNotification.apply {
                accessibilityDelegate = blindImageViewButtonFocused(this, requireContext())
                setOnClickListener {
                    findNavController().navigate(BlindMainFragmentDirections.actionBlindMainFragmentToBlindNotificationFragment())
                }
            }

            btnSetting.apply {
                accessibilityDelegate = blindImageViewButtonFocused(this, requireContext())
                setOnClickListener {
                    findNavController().navigate(BlindMainFragmentDirections.actionBlindMainFragmentToBlindSettingFragment())
                }
            }

            btnScanText.apply {
                accessibilityDelegate = accessibilityEvent(this, requireContext())
                setOnClickListener {
                    checkPermission(VIEW_SCAN_TEXT, Manifest.permission.CAMERA)
                }
            }
            btnFindObject.apply {
                accessibilityDelegate = accessibilityEvent(this, requireContext())
                setOnClickListener {
                    checkPermission(VIEW_FIND_OBJECT, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                }
            }
            btnScanObstacle.apply {
                accessibilityDelegate = accessibilityEvent(this, requireContext())
                setOnClickListener {
                    checkPermission(VIEW_SCAN_OBSTACLE, Manifest.permission.CAMERA)
                }
            }
            btnHelp.apply {
                accessibilityDelegate = accessibilityEvent(this, requireContext())
                setOnClickListener {
                    checkPermission(VIEW_HELP, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS)
                }
            }
            btnComplaints.apply {
                accessibilityDelegate = accessibilityEvent(this, requireContext())
                setOnClickListener {
                    checkPermission(VIEW_COMPLAINTS, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }
    }

    private fun checkPermission(direction: Int, vararg permissions: String) {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                when(direction){
                    VIEW_SCAN_TEXT -> startActivity(Intent(requireContext(), ScanTextActivity::class.java))
                    VIEW_FIND_OBJECT -> startActivity(Intent(requireContext(), FindObjectActivity::class.java))
                    VIEW_SCAN_OBSTACLE -> startActivity(Intent(requireContext(), ScanObstacleActivity::class.java))
                    VIEW_HELP -> startActivity(Intent(requireContext(), BlindHelpActivity::class.java).apply {
                        val gender = requireActivity().intent.getStringExtra("Gender")
                        putExtra("Gender", gender)
                    })
                    VIEW_COMPLAINTS -> findNavController().navigate(BlindMainFragmentDirections.actionBlindMainFragmentToComplaintsFragment())
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