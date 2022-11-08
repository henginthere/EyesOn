package com.d201.eyeson.view.angel.complaints

import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.util.Log
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
import com.d201.eyeson.view.angel.ReturnConfirmListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream


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
        }
        lifecycleScope.launch{
            complaintsViewModel.complaints.collectLatest {
                complaints = it!!
            }
        }

    }

    private fun initView() {
        binding.apply {
            vm = complaintsViewModel

            btnGoSafetyEReport.setOnClickListener {
                copyComplaints()
                imageUrlToCacheFileAsync(requireContext(), complaints.image!!)
                openWebPage()
            }
            btnReject.setOnClickListener {
                ReturnComplaintsDialog(complaintsViewModel.complaints.value!!, returnConfirmListener).let {
                    it.show(parentFragmentManager, "ReturnComplaints")
                }
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
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val newFile = File(
                        context.cacheDir.path,
                        complaints.image!!
                    ).apply {
                        createNewFile()
                    }
                    FileOutputStream(newFile).use {
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, it)
                    }
                    val values = ContentValues()
                    values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis())
                    values.put(Images.Media.MIME_TYPE, "image/jpeg")
                    values.put(MediaStore.MediaColumns.DATA, "${context.cacheDir.path}/${complaints.image}")
                    Log.d(TAG, "onResourceReady: ${context.cacheDir.path}/${complaints.image}")
                    context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
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
            findNavController().popBackStack()
        }
    }
}