package com.d201.eyeson.view.blind

import com.d201.eyeson.R
import com.d201.eyeson.base.BaseActivity
import com.d201.eyeson.databinding.ActivityBlindMainBinding

class BlindMainActivity : BaseActivity<ActivityBlindMainBinding>(R.layout.activity_blind_main) {
    override fun init() {
        initView()
    }

    private fun initView(){
        supportFragmentManager.beginTransaction().replace(R.id.frame_blind_main, BlindMainFragment()).commit()
    }
}