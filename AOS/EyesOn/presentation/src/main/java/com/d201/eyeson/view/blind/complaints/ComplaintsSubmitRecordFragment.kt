package com.d201.eyeson.view.blind.complaints

import android.location.Location
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d201.domain.model.Complaints
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentComplaintsSubmitRecordBinding
import com.d201.eyeson.util.accessibilityEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File

private const val TAG ="ComplaintsSubmitRecordFragment"
@AndroidEntryPoint
class ComplaintsSubmitRecordFragment : BaseFragment<FragmentComplaintsSubmitRecordBinding>(R.layout.fragment_complaints_submit_record) {

    private val args: ComplaintsSubmitRecordFragmentArgs by navArgs()
    private val viewModel: ComplaintsSubmitRecordViewModel by viewModels()
    private lateinit var location: Location

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
            setLocationRepository(requireContext())
            enableLocationServices()
            locationRepository?.let{
                it.observe(viewLifecycleOwner){
                    it.let {
                        this.setLocationItem(it!!)
                    }
                }
            }
            location?.observe(viewLifecycleOwner){
                Log.d(TAG, "initViewModel: GPS LOCATION : ${it}")
                this@ComplaintsSubmitRecordFragment.location = it
            }
        }
    }

    private fun initView() {
        binding.apply {
            vm = viewModel

            ivTest.setImageURI(args.image)
            Log.d(TAG, "initView: ${args.image}")
            val file = File("/data/user/0/com.d201.eyeson/cache/image/20221107_1729254083986542103703830.jpg")
            if(file.exists()){
                Log.d(TAG, "initView: $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$")
            }
            
            btnRecord.setOnClickListener {
                record()
            }
            btnSubmit.apply {
                accessibilityDelegate = accessibilityEvent(this, requireContext())
                setOnClickListener {
                    Log.d(TAG, "initView: ${location.longitude},${location.latitude}")
                    ivTest.setImageURI(args.image)
                    viewModel.submitComplaints(Complaints("${location.longitude},${location.latitude}",viewModel.recordText.value), args.image)

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
