package com.d201.eyeson.view.binding

import android.net.Uri
import android.util.Log
import android.util.TypedValue
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