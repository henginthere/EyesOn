package com.d201.eyeson.view.blind.complaints

import android.location.Location
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d201.data.mapper.mapperToComplaintsRequest
import com.d201.domain.model.Complaints
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentComplaintsSubmitRecordBinding
import com.d201.eyeson.util.accessibilityEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "ComplaintsSubmitRecordFragment"

@AndroidEntryPoint
class ComplaintsSubmitRecordFragment :
    BaseFragment<FragmentComplaintsSubmitRecordBinding>(R.layout.fragment_complaints_submit_record) {

    private val args: ComplaintsSubmitRecordFragmentArgs by navArgs()
    private val viewModel: ComplaintsSubmitRecordViewModel by viewModels()
    private lateinit var location: Location

    override fun init() {
        initView()
        initViewModel()
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            viewModel.statusSTT.collectLatest {
                when (it) {
                    true -> binding.btnRecord.playAnimation()
                    false -> binding.btnRecord.pauseAnimation()
                }
            }
        }
        viewModel.apply {
            initTTS(requireContext())
            successResultEvent.observe(viewLifecycleOwner) {
                showToast("민원이 접수되었습니다")
                findNavController().popBackStack()
            }
            setLocationRepository(requireContext())
            enableLocationServices()
            locationRepository?.let {
                it.observe(viewLifecycleOwner) {
                    it.let {
                        this.setLocationItem(it!!)
                    }
                }
            }
            location?.observe(viewLifecycleOwner) {
                Log.d(TAG, "initViewModel: GPS LOCATION : ${it}")
                this@ComplaintsSubmitRecordFragment.location = it
                binding.btnSubmit.isEnabled = true
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
                    val comp = Complaints(
                        "${location.longitude},${location.latitude}",
                        viewModel.recordText.value
                    )
                    val c = comp.mapperToComplaintsRequest()
                    viewModel.submitComplaints(comp, args.image)
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

    private fun record() {
        viewModel.startRecord(requireContext())
    }


}
