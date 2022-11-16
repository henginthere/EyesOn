package com.d201.eyeson.view.angel.complaints

import android.Manifest
import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.d201.domain.model.Complaints
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentComplaintsDetailBinding
import com.d201.eyeson.util.S3_URL
import com.d201.eyeson.view.angel.RegisterComplaintsListener
import com.d201.eyeson.view.angel.ReturnConfirmListener
import com.d201.eyeson.view.angel.TitleConfirmListener
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


private const val TAG = "ComplaintsDetailFragment"
@AndroidEntryPoint
class ComplaintsDetailFragment : BaseFragment<FragmentComplaintsDetailBinding>(R.layout.fragment_complaints_detail) {

    private val complaintsViewModel: ComplaintsViewModel by viewModels()
    private val args: ComplaintsDetailFragmentArgs by navArgs()
    private lateinit var complaints: Complaints

    override fun init() {
        initView()
        initViewModel()
    }

    private fun initViewModel() {
        complaintsViewModel.apply {
            getComplaints(args.complaintsSeq)
            successResultEvent.observe(viewLifecycleOwner){
                showToast(it)
                findNavController().popBackStack()
            }
        }
        lifecycleScope.launch{
            complaintsViewModel.complaints.collectLatest {
                if(it != null) {
                    complaints = it
                }
            }
        }

    }

    private fun initView() {
        binding.apply {
            vm = complaintsViewModel

            btnGoSafetyEReport.setOnClickListener {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    copyComplaints()
                    imageUrlToCacheFileAsync(requireContext(), complaints.image!!)
                    openWebPage()
                    binding.btnRegisterTitle.visibility = View.VISIBLE
                }else {
                    checkPermission()
                }
            }
            btnReject.setOnClickListener {
                ReturnComplaintsDialog(complaintsViewModel.complaints.value!!, returnConfirmListener).let {
                    it.show(parentFragmentManager, "ReturnComplaints")
                }
            }
            btnRegisterTitle.setOnClickListener {
                RegisterTitleDialog(complaintsViewModel.complaints.value!!, titleConfirmListener).let{
                    it.show(parentFragmentManager, "RegisterTitle")
                }
            }
            btnComplaintsDone.setOnClickListener {
                RegisterComplaintsResultDialog(complaintsViewModel.complaints.value!!, registerComplaintsListener).show(parentFragmentManager, "RegisterResult")
            }
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun copyComplaints(){
        val clipboard = requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", "${complaintsViewModel.complaints.value!!.address}\n${complaintsViewModel.complaints.value!!.content}")
        clipboard.setPrimaryClip(clip)
    }

    private fun openWebPage(){
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.safetyreport.go.kr/#safereport/safereport")))
    }

    fun imageUrlToCacheFileAsync(context: Context, url: String){
        Glide.with(context)
            .asBitmap()
            .load("$S3_URL$url")
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    saveMediaToStorage(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                }
            })
    }

    private val returnConfirmListener = object : ReturnConfirmListener{
        override fun onClick(complaints: Complaints) {
            complaintsViewModel.returnComplaints(complaints)
        }
    }

    private val titleConfirmListener = object : TitleConfirmListener{
        override fun onClick(complaints: Complaints) {
            complaintsViewModel.submitComplaints(complaints)
        }
    }

    private val registerComplaintsListener = object : RegisterComplaintsListener {
        override fun onClick(complaints: Complaints) {
            complaintsViewModel.completeComplaints(complaints)
        }
    }

    fun saveMediaToStorage(bitmap: Bitmap) {
        val filename = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context?.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }
        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
    }

    private fun checkPermission() {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                copyComplaints()
                imageUrlToCacheFileAsync(requireContext(), complaints.image!!)
                openWebPage()
                binding.btnRegisterTitle.visibility = View.VISIBLE
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                showToast("권한을 허용해야 이용이 가능합니다.")
            }

        }
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setDeniedMessage("권한을 허용해주세요. [설정] > [앱 및 알림] > [고급] > [앱 권한]")
            .setPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .check()
    }
}