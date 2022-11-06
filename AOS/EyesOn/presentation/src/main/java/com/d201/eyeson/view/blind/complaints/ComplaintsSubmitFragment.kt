package com.d201.eyeson.view.blind.complaints

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentComplaintsSubmitBinding
import com.d201.eyeson.util.accessibilityEvent
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream

private const val TAG = "ComplaintsSubmitFragment"
@AndroidEntryPoint
class ComplaintsSubmitFragment : BaseFragment<FragmentComplaintsSubmitBinding>(R.layout.fragment_complaints_submit) {

    private var bitmap: Bitmap? = null
    override fun init() {
        takePicture()
        initView()
    }

    private fun initView() {
        binding.apply {
            btnCamera.apply {
                accessibilityDelegate = accessibilityEvent(this, requireContext())
                setOnClickListener {
                    takePicture()
                }
            }
            btnBack.apply {
                accessibilityDelegate = accessibilityEvent(this, requireContext())
                setOnClickListener {
                    findNavController().popBackStack()
                }
            }
            btnSubmit.apply {
                accessibilityDelegate = accessibilityEvent(this, requireContext())
                setOnClickListener {
                    val fileName = saveBitmap()
                    if(saveBitmap().isNotBlank()){
                        findNavController().navigate(ComplaintsSubmitFragmentDirections.actionComplaintsSubmitFragmentToComplaintsSubmitRecordFragment(fileName))
                    }else{
                        showToast("사진을 촬영해 주세요")
                    }
                }
            }
        }
    }

    private fun takePicture(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { photoIntent ->
            photoIntent.resolveActivity(requireContext().packageManager)?.also {
                cameraIntentLauncher.launch(photoIntent)
            }
        }
    }

    private val cameraIntentLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK){
            val bitmap = it.data!!.extras!!.get("data") as Bitmap
            binding.ivSelectedImage.setImageBitmap(bitmap)
            this.bitmap = bitmap
        }
    }

    private fun saveBitmap(): String{
        if(bitmap != null){
            try {
                val file = File(requireContext().cacheDir, "${System.currentTimeMillis()}.jpg")
                file.createNewFile()
                val fos = FileOutputStream(file)
                bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.close()
                return file.name
            }catch (e: Exception){
                Log.d(TAG, "initView: ${e.message}")
            }
        }
        return ""
    }

}