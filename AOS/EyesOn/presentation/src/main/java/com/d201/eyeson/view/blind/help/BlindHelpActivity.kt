package com.d201.eyeson.view.blind.help

import android.graphics.RectF
import android.widget.ImageButton
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseActivity
import com.d201.eyeson.databinding.ActivityAngelHelpBinding
import com.d201.eyeson.util.accessibilityEvent
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "BlindHelpActivity"
@AndroidEntryPoint
class BlindHelpActivity : BaseActivity<ActivityAngelHelpBinding>(R.layout.activity_blind_help){

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var btnBack:ImageButton

    override fun init() {
        initView()
        initListener()
    }

    private fun initView(){
        navHostFragment = supportFragmentManager.findFragmentById(R.id.frame_blind_help) as NavHostFragment
        navController = navHostFragment.navController

    }
    private fun initListener(){
        btnBack = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}