package com.d201.eyeson.view.angel.help

import android.content.Intent
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseActivity
import com.d201.eyeson.databinding.ActivityAngelHelpBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "AngelHelpActivity"

@AndroidEntryPoint
class AngelHelpActivity : BaseActivity<ActivityAngelHelpBinding>(R.layout.activity_angel_help) {

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun init() {
        initView()
    }

    private fun initView() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.frame_angel_help) as NavHostFragment
        navController = navHostFragment.navController
    }

}