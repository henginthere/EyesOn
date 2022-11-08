package com.d201.eyeson.view.blind.complaints

import android.Manifest
import androidx.navigation.fragment.findNavController
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentComplaintsBinding
import com.d201.eyeson.util.accessibilityEvent
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "ComplaintsFragment"
@AndroidEntryPoint
class ComplaintsFragment : BaseFragment<FragmentComplaintsBinding>(R.layout.fragment_complaints) {

    override fun init() {
        initView()
        initViewModel()
    }

    private fun initViewModel() {

    }

    private fun initView() {
        binding.apply {
            btnBack.apply {
                accessibilityDelegate = accessibilityEvent(this, requireContext())
                setOnClickListener {
                    findNavController().popBackStack()
                }
            }
            btnMyComplaints.apply {
                accessibilityDelegate = accessibilityEvent(this, requireContext())
                setOnClickListener {
                    findNavController().navigate(ComplaintsFragmentDirections.actionComplaintsFragmentToMyComplaintsFragment())
                }
            }
            btnNewComplaints.apply {
                accessibilityDelegate = accessibilityEvent(this, requireContext())
                setOnClickListener {
                    checkPermission()
                }
            }
        }
    }

    private fun checkPermission() {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                findNavController().navigate(ComplaintsFragmentDirections.actionComplaintsFragmentToComplaintsSubmitFragment())
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                showToast("권한을 허용해야 이용이 가능합니다.")
                findNavController().popBackStack()
            }

        }
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setDeniedMessage("권한을 허용해주세요. [설정] > [앱 및 알림] > [고급] > [앱 권한]")
            .setPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .check()
    }


}