package com.d201.eyeson.view.blind.notification

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.d201.domain.model.Noti
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentBlindNotiBinding
import com.d201.eyeson.util.accessibilityEvent
import com.d201.eyeson.view.blind.NotiClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "BlindNotiFragment"

@AndroidEntryPoint
class BlindNotiFragment : BaseFragment<FragmentBlindNotiBinding>(R.layout.fragment_blind_noti) {

    private val blindNotiViewModel: BlindNotiViewModel by viewModels()
    private lateinit var blindNotiAdapter: BlindNotiAdapter

    override fun init() {
        initView()
        initViewModelCallback()
    }

    private fun initViewModelCallback() {
        lifecycleScope.launch {
            blindNotiViewModel.notis.collectLatest { notis ->
                blindNotiAdapter.submitList(notis)
            }
        }

    }

    private fun initView() {
        blindNotiViewModel.selectAllNotis()
        blindNotiAdapter = BlindNotiAdapter(notiClickListener)
        binding.apply {
            notiVM = blindNotiViewModel
            rvNotification.adapter = blindNotiAdapter
            btnBack.apply {
                accessibilityDelegate = accessibilityEvent(this, requireContext())
                setOnClickListener { findNavController().popBackStack() }
            }
        }
    }

    private val notiClickListener = object : NotiClickListener {
        override fun onClick(noti: Noti, position: Int) {
            blindNotiViewModel.deleteNoti(noti)
        }
    }
}