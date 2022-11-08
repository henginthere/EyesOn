package com.d201.eyeson.view.blind

import android.content.Intent
import androidx.navigation.fragment.findNavController
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentBlindMainBinding
import com.d201.eyeson.view.blind.findobject.FindObjectActivity
import com.d201.eyeson.view.blind.findobject.FindObjectFragment
import com.d201.eyeson.view.blind.help.BlindHelpActivity
import com.d201.eyeson.view.blind.scanobstacle.ScanObstacleActivity
import com.d201.eyeson.view.blind.scantext.ScanTextActivity
import com.d201.eyeson.view.blind.scantext.ScanTextFragment
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "BlindMainFragment"
@AndroidEntryPoint
class BlindMainFragment : BaseFragment<FragmentBlindMainBinding>(R.layout.fragment_blind_main) {

    override fun init() {
        initListener()
    }

    private fun initListener() {
        binding.apply {
            btnNotification.setOnClickListener {
                findNavController().navigate(BlindMainFragmentDirections.actionBlindMainFragmentToBlindNotificationFragment())
            }

            btnSetting.setOnClickListener {
                findNavController().navigate(BlindMainFragmentDirections.actionBlindMainFragmentToBlindSettingFragment())
            }

            btnScanText.setOnClickListener {
                startActivity(Intent(requireContext(), ScanTextActivity::class.java))
            }
            btnFindObject.setOnClickListener {
                startActivity(Intent(requireContext(), FindObjectActivity::class.java))
            }
            btnScanObstacle.setOnClickListener {
                startActivity(Intent(requireContext(), ScanObstacleActivity::class.java))
            }
            btnHelp.setOnClickListener {
                startActivity(Intent(requireContext(), BlindHelpActivity::class.java).apply {
                    val gender = requireActivity().intent.getStringExtra("Gender")
                    putExtra("Gender", gender)
                })
            }
            btnComplaints.setOnClickListener {
                findNavController().navigate(BlindMainFragmentDirections.actionBlindMainFragmentToComplaintsFragment())
            }
        }
    }


}