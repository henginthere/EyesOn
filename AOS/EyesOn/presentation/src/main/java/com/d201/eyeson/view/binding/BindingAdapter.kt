package com.d201.eyeson.view.binding

import android.net.Uri
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.d201.eyeson.R
import com.d201.eyeson.util.S3_URL


private const val TAG = "BindingAdapter"

@BindingAdapter("imageLoader")
fun imageLoader(view: ImageView, src: String?) {
    Log.d(TAG, "imageLoader: ${S3_URL}$src")
    Glide.with(view.context)
        .load(Uri.parse("${S3_URL}$src"))
        .error(R.drawable.icon_no_image)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(view)
}

@BindingAdapter("textSize")
fun bindTextSize(textView: TextView, title: String?) {
    if(title == null){
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24.toFloat())
    }else{
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.toFloat())
    }
}

@BindingAdapter("state")
fun bindState(textView: TextView, state: String?){
    if(state != null){
        when(state){
            "PROGRESS_IN" -> {
                textView.text = "민원 처리중"
            }
            "RETURN" -> {
                textView.text = "민원 반환됨"
            }
            "REGIST_DONE" -> {
                textView.text = "민원 등록 완료"
            }
            "PROGRESS_DONE" -> {
                textView.text = "민원 처리 완료"
            }
        }
    }
}