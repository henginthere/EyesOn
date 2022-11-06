package com.d201.eyeson.view.blind.complaints

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentComplaintsSubmitRecordBinding
import com.d201.eyeson.util.accessibilityEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG ="ComplaintsSubmitRecordFragment"
@AndroidEntryPoint
class ComplaintsSubmitRecordFragment : BaseFragment<FragmentComplaintsSubmitRecordBinding>(R.layout.fragment_complaints_submit_record) {

    private val args: ComplaintsSubmitRecordFragmentArgs by navArgs()
    private val viewModel: ComplaintsSubmitRecordViewModel by viewModels()

    override fun init() {
        initView()
        initViewModel()
    }

    private fun initViewModel(){
        lifecycleScope.launch {
            viewModel.statusSTT.collectLatest {
                when(it){
                    true -> binding.btnRecord.playAnimation()
                    false -> binding.btnRecord.pauseAnimation()
                }
            }
        }
        viewModel.apply {
            initTTS(requireContext())
            successResultEvent.observe(viewLifecycleOwner){
                showToast("민원이 접수되었습니다")
                findNavController().popBackStack()
            }
        }
    }

    private fun initView() {
        binding.apply {
            vm = viewModel

            btnRecord.setOnClickListener {
                record()
            }
            btnSubmit.apply {
                accessibilityDelegate = accessibilityEvent(this, requireContext())
                setOnClickListener {

                }
            }
            btnBack.apply {
                accessibilityDelegate = accessibilityEvent(this, requireContext())
                setOnClickListener {
                    findNavController().popBackStack()
                }
            }

        }
    }

    private fun record(){
        viewModel.startRecord(requireContext())
    }


}
