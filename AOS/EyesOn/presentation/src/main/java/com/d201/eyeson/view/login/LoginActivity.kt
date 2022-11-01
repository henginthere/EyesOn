package com.d201.eyeson.view.login

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseActivity
import com.d201.eyeson.databinding.ActivityLoginBinding
import com.d201.eyeson.view.login.login.LoginFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "LoginActivity"
@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun init() {
        initView()
    }

    private fun initView(){
        navHostFragment = supportFragmentManager.findFragmentById(R.id.frame_login) as NavHostFragment
        navController = navHostFragment.navController
    }
}