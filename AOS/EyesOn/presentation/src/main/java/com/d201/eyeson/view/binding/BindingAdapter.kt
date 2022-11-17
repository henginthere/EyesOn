package com.d201.eyeson.view.binding

import android.net.Uri
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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
    if (title == null) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24.toFloat())
    } else {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.toFloat())
    }
}

@BindingAdapter("complaintsDoneVisible")
fun complaintsDoneVisible(view: Button, state: String?) {
    if (!state.isNullOrEmpty()) {
        if (state == "REGIST_DONE") {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }
}

@BindingAdapter("complaintsRegistVisible")
fun complaintsRegistVisible(view: ConstraintLayout, state: String?) {
    if (!state.isNullOrEmpty()) {
        if (state == "PROGRESS_IN") {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.INVISIBLE
        }
    }
}