package com.d201.eyeson.view.blind

import android.graphics.Color
import android.view.WindowManager
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseActivity
import com.d201.eyeson.databinding.ActivityBlindMainBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "BlindMainActivity"

@AndroidEntryPoint
class BlindMainActivity : BaseActivity<ActivityBlindMainBinding>(R.layout.activity_blind_main) {

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun init() {
        val window = window
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
        window.apply {
            //상태바
            statusBarColor = Color.BLACK
            //상태바 아이콘(true: 검정 / false: 흰색)
            WindowInsetsControllerCompat(this, this.decorView).isAppearanceLightStatusBars = false
        }
        initView()
    }

    private fun initView() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.frame_blind_main) as NavHostFragment
        navController = navHostFragment.navController
    }

}