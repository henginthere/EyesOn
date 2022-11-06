package com.d201.eyeson.util

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.accessibility.AccessibilityEventCompat
import com.d201.eyeson.R

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
