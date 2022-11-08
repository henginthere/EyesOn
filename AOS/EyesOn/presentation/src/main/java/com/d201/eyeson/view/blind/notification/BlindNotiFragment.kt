package com.d201.eyeson.view.blind.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.d201.domain.model.Noti
import com.d201.domain.utils.ResultType
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentBlindNotiBinding
import com.d201.eyeson.service.FirebaseCloudMessagingService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.log

private const val TAG = "BlindNotiFragment"
@AndroidEntryPoint
class BlindNotiFragment : BaseFragment<FragmentBlindNotiBinding>(R.layout.fragment_blind_noti){

    private val blindNotiViewModel : BlindNotiViewModel by viewModels()
    private lateinit var blindNotiAdapter: BlindNotiAdapter

    override fun init() {
        initView()
        initViewModelCallback()
    }

    private fun initViewModelCallback() {
        lifecycleScope.launch {
            blindNotiViewModel.notis.collectLatest {
                if(it is ResultType.Success){
                    Log.d(TAG, "initViewModelCallback: ${it.data}")
                    blindNotiAdapter.submitList(it.data)
                }
            }
        }

    }

    private fun initView() {
        blindNotiViewModel.selectAllNotis()
        blindNotiAdapter = BlindNotiAdapter()
        binding.apply {
            notiVM = blindNotiViewModel
            rvNotification.adapter = blindNotiAdapter
        }
    }
}