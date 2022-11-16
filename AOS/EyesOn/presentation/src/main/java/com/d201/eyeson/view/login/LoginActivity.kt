package com.d201.eyeson.view.login

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseActivity
import com.d201.eyeson.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "LoginActivity"

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun init() {
        initView()
    }

    private fun initView() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.frame_login) as NavHostFragment
        navController = navHostFragment.navController
    }
}