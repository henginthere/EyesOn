package com.d201.eyeson.view.blind.complaints

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentComplaintsSubmitBinding
import com.d201.eyeson.util.accessibilityEvent
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "ComplaintsSubmitFragment"
@AndroidEntryPoint
class ComplaintsSubmitFragment : BaseFragment<FragmentComplaintsSubmitBinding>(R.layout.fragment_complaints_submit) {

    private var bitmap: Bitmap? = null
    private var photoURI: Uri? = null
    private var currentPhotoPath: String? = null

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
                    Log.d(TAG, "initView: $photoURI")
                    if(photoURI != null){
                        findNavController().navigate(ComplaintsSubmitFragmentDirections.actionComplaintsSubmitFragmentToComplaintsSubmitRecordFragment(currentPhotoPath!!))
                    }else{
                        showToast("사진을 촬영해 주세요")
                    }
                }
            }
        }
    }

    private val cameraIntentLauncher: ActivityResultLauncher<Uri> = registerForActivityResult(ActivityResultContracts.TakePicture()){
        if(it){
            photoURI?.let {
                binding.ivSelectedImage.setImageURI(it)
                Log.d(TAG, "photoURI = ${photoURI!!.path}")
            }
        }else{
            photoURI = null
        }
    }

    private fun takePicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (e: IOException) {
                    null
                }
                photoFile?.also {
                    photoURI = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}", photoFile)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    cameraIntentLauncher.launch(photoURI)
                }
            }
        }
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = File("${requireContext().cacheDir}/image")
        if(!storageDir.exists()){
            storageDir.mkdirs()
        }
        return File.createTempFile(
            "${timeStamp}",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

}