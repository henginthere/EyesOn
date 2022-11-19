package com.d201.eyeson.view.intro

import android.graphics.Color
import androidx.core.view.WindowInsetsControllerCompat
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseActivity
import com.d201.eyeson.databinding.ActivityIntroBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "IntroActivity"

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding>(R.layout.activity_intro) {

    override fun init() {
        window.apply {
            //상태바
            statusBarColor = Color.BLACK
            //상태바 아이콘(true: 검정 / false: 흰색)
            WindowInsetsControllerCompat(this, this.decorView).isAppearanceLightStatusBars = false
        }
    }

}