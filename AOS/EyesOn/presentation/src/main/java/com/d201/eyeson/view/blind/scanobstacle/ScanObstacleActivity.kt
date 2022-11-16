package com.d201.eyeson.view.blind.scanobstacle

import com.d201.depth.depth.common.FullScreenHelper
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseActivity
import com.d201.eyeson.databinding.ActivityScanObstacleBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "ScanObstacleActivity"

@AndroidEntryPoint
class ScanObstacleActivity :
    BaseActivity<ActivityScanObstacleBinding>(R.layout.activity_scan_obstacle) {
    override fun init() {
        initView()
    }

    private fun initView() {
        binding.apply {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_scan_obstacle, ScanObstacleFragment()).commit()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        FullScreenHelper.setFullScreenOnWindowFocusChanged(this, hasFocus)
    }
}