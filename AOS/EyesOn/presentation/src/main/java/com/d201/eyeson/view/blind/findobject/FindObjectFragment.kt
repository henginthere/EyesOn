package com.d201.eyeson.view.blind.findobject

import androidx.lifecycle.lifecycleScope
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentFindObjectBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.concurrent.timer
import kotlin.concurrent.timerTask

private const val TAG = "FindObjectFragment"
@AndroidEntryPoint
class FindObjectFragment : BaseFragment<FragmentFindObjectBinding>(R.layout.fragment_find_object) {

    override fun init() {
    }

}