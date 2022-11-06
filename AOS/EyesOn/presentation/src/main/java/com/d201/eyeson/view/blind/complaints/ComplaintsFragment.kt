package com.d201.eyeson.view.blind.complaints

import androidx.navigation.fragment.findNavController
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentComplaintsBinding
import com.d201.eyeson.util.accessibilityEvent
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "ComplaintsFragment"
@AndroidEntryPoint
class ComplaintsFragment : BaseFragment<FragmentComplaintsBinding>(R.layout.fragment_complaints) {

    override fun init() {
        initView()
        initViewModel()
    }

    private fun initViewModel() {

    }

    private fun initView() {
        binding.apply {
            btnBack.apply {
                accessibilityDelegate = accessibilityEvent(this, requireContext())
                setOnClickListener {
                    findNavController().popBackStack()
                }
            }
            btnMyComplaints.apply {
                accessibilityDelegate = accessibilityEvent(this, requireContext())
                setOnClickListener {
                    findNavController().navigate(ComplaintsFragmentDirections.actionComplaintsFragmentToMyComplaintsFragment())
                }
            }
            btnNewComplaints.apply {
                accessibilityDelegate = accessibilityEvent(this, requireContext())
                setOnClickListener {
                    findNavController().navigate(ComplaintsFragmentDirections.actionComplaintsFragmentToComplaintsSubmitFragment())
                }
            }
        }
    }


}