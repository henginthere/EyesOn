package com.d201.eyeson.view.angel

import com.d201.eyeson.R
import com.d201.eyeson.base.BaseActivity
import com.d201.eyeson.databinding.ActivityAngelMainBinding
import com.d201.eyeson.view.blind.BlindMainFragment

class AngelMainActivity : BaseActivity<ActivityAngelMainBinding>(R.layout.activity_angel_main){
    override fun init() {
        supportFragmentManager.beginTransaction().replace(R.id.frame_angel_main, AngelMainFragment()).commit()
    }
}