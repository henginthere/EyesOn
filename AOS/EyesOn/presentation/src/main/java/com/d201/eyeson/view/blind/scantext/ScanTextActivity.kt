package com.d201.eyeson.view.blind.scantext

import com.d201.eyeson.R
import com.d201.eyeson.base.BaseActivity
import com.d201.eyeson.databinding.ActivityScanTextBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "ScanTextActivity"
@AndroidEntryPoint
class ScanTextActivity : BaseActivity<ActivityScanTextBinding>(R.layout.activity_scan_text) {

    override fun init() {
        initView()
    }

    private fun initView(){
        binding.apply {
            supportFragmentManager.beginTransaction().replace(R.id.frame_scan_text, ScanTextFragment()).commit()
        }
    }

}