package com.d201.eyeson.view.blind.findobject

import com.d201.eyeson.R
import com.d201.eyeson.base.BaseActivity
import com.d201.eyeson.databinding.ActivityFindObjectBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "FindObjectActivity"
@AndroidEntryPoint
class FindObjectActivity : BaseActivity<ActivityFindObjectBinding>(R.layout.activity_find_object) {

    override fun init() {
        initView()
    }

    private fun initView(){
        binding.apply {
            supportFragmentManager.beginTransaction().replace(R.id.frame_find_object, FindObjectFragment()).commit()
        }
    }

}