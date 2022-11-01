package com.d201.eyeson.view.login.join

import androidx.navigation.fragment.findNavController
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentSelectRoleBinding
import com.d201.eyeson.util.ANGEL
import com.d201.eyeson.util.BLIND

private const val TAG = "SelectRoleFragment"
class SelectRoleFragment : BaseFragment<FragmentSelectRoleBinding>(R.layout.fragment_select_role) {

    override fun init() {
        initView()
    }

    private fun initView() {
        binding.apply {
            btnSelectAngel.setOnClickListener {
                findNavController().navigate(SelectRoleFragmentDirections.actionSelectRoleFragmentToSelectGenderFragment(ANGEL))
            }
            btnSelectBlind.setOnClickListener {
                findNavController().navigate(SelectRoleFragmentDirections.actionSelectRoleFragmentToSelectGenderFragment(BLIND))
            }
        }
    }

}