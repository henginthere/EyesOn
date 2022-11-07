package com.d201.eyeson.util

import android.content.Context
import android.content.res.ColorStateList
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.accessibility.AccessibilityEventCompat
import com.d201.eyeson.R
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

private const val TAG ="EXTENSIONS"

private fun blindTextViewFocused(context: Context, view: TextView, eventType: Int){
    when(eventType){
        AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUSED -> {
            view.setBackgroundResource(R.drawable.btn_border_yellow_focus)
            view.setTextColor(context.getColor(R.color.black))
        }
        AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED -> {
            view.setBackgroundResource(R.drawable.btn_border_yellow)
            view.setTextColor(context.getColor(R.color.blind_yellow))
        }
    }
}

private fun blindBackButtonViewFocused(view: ImageButton, eventType: Int){
    when(eventType){
        AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUSED -> {
            view.setImageResource(R.drawable.btn_blind_back_black)
            view.setBackgroundResource(R.color.blind_yellow)
        }
        AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED -> {
            view.setImageResource(R.drawable.btn_blind_back_yellow)
            view.setBackgroundResource(R.color.none)
        }
    }
}

private fun blindImageViewFocused(context: Context, view: ImageView, eventType: Int){
    when(eventType){
        AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUSED -> {
            view.imageTintList = ColorStateList.valueOf(context.getColor(R.color.black))
            view.setBackgroundResource(R.drawable.btn_border_yellow_focus)
        }
        AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED -> {
            view.imageTintList = ColorStateList.valueOf(context.getColor(R.color.blind_yellow))
            view.setBackgroundResource(R.drawable.btn_border_yellow)
        }
    }
}

fun accessibilityEvent(view: View, context: Context) = object : View.AccessibilityDelegate(){
    override fun onInitializeAccessibilityEvent(host: View, event: AccessibilityEvent) {
        super.onInitializeAccessibilityEvent(host, event)
        when(view){
            is TextView -> {
                blindTextViewFocused(context, view, event.eventType)
            }
            is ImageButton -> {
                blindBackButtonViewFocused(view, event.eventType)
            }
            is ImageView -> {
                blindImageViewFocused(context, view, event.eventType)
            }
        }
    }
}

fun Any.objectToPartBody(key: String, value: Any): MultipartBody.Part{
    return MultipartBody.Part.createFormData(
        name = key,
        value = "application/json; charset=utf-8"
    )
}

fun Uri.imageUriToPartBody(key: String, file: File): MultipartBody.Part{
    Log.d(TAG, "imageUriToPartBody: ${file}")
    return MultipartBody.Part.createFormData(
        name = key,
        filename = file.name,
        body = file.asRequestBody("image/*".toMediaType())
    )
}
