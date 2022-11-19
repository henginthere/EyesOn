package com.d201.eyeson.view.blind.findobject

import android.graphics.Color
import androidx.core.view.WindowInsetsControllerCompat
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseActivity
import com.d201.eyeson.databinding.ActivityFindObjectBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "FindObjectActivity"

@AndroidEntryPoint
class FindObjectActivity : BaseActivity<ActivityFindObjectBinding>(R.layout.activity_find_object) {

    override fun init() {
        window.apply {
            //상태바
            statusBarColor = Color.BLACK
            //상태바 아이콘(true: 검정 / false: 흰색)
            WindowInsetsControllerCompat(this, this.decorView).isAppearanceLightStatusBars = false
        }
        initView()
    }

    private fun initView() {
        binding.apply {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_find_object, FindObjectFragment()).commit()
        }
    }

}