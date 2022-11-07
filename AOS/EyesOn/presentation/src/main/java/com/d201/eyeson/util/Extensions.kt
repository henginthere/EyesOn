package com.d201.eyeson.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.media.Image
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicYuvToRGB
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
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.nio.ByteBuffer

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

fun Any.objectToRequestBody(value: Any): RequestBody{
    return Gson().toJson(value).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
}

fun Any.objectToMultipartPart(value: Any): MultipartBody.Part{
    return MultipartBody.Part.createFormData(name = "params", null, objectToRequestBody(value))
}

fun String.imagePathToPartBody(key: String, file: File): MultipartBody.Part{
    Log.d(TAG, "imageUriToPartBody: ${file}")
    return MultipartBody.Part.createFormData(
        name = key,
        filename = file.name,
        body = file.asRequestBody("image/*".toMediaType())
    )
}

fun RotateBitmap(source: Bitmap, angle: Float): Bitmap? {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
}

fun imageToBitmap(image: Image, mContext: Context?): Bitmap? {

    // Get the YUV data
    val yuvBytes: ByteBuffer = imageToByteBuffer(image)!!

    // Convert YUV to RGB
    val rs = RenderScript.create(mContext)
    val bitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
    val allocationRgb = Allocation.createFromBitmap(rs, bitmap)
    val allocationYuv = Allocation.createSized(rs, Element.U8(rs), yuvBytes.array().size)
    allocationYuv.copyFrom(yuvBytes.array())
    val scriptYuvToRgb = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs))
    scriptYuvToRgb.setInput(allocationYuv)
    scriptYuvToRgb.forEach(allocationRgb)
    allocationRgb.copyTo(bitmap)
    allocationYuv.destroy()
    allocationRgb.destroy()
    rs.destroy()
    return bitmap
    // Release
}

private fun imageToByteBuffer(image: Image): ByteBuffer? {
    val crop = image.cropRect
    val width = crop.width()
    val height = crop.height()
    val planes = image.planes
    val rowData = ByteArray(planes[0].rowStride)
    val bufferSize = width * height * ImageFormat.getBitsPerPixel(ImageFormat.YUV_420_888) / 8
    val output = ByteBuffer.allocateDirect(bufferSize)
    var channelOffset = 0
    var outputStride = 0
    for (planeIndex in 0..2) {
        if (planeIndex == 0) {
            channelOffset = 0
            outputStride = 1
        } else if (planeIndex == 1) {
            channelOffset = width * height + 1
            outputStride = 2
        } else if (planeIndex == 2) {
            channelOffset = width * height
            outputStride = 2
        }
        val buffer = planes[planeIndex].buffer
        val rowStride = planes[planeIndex].rowStride
        val pixelStride = planes[planeIndex].pixelStride
        val shift = if (planeIndex == 0) 0 else 1
        val widthShifted = width shr shift
        val heightShifted = height shr shift
        buffer.position(rowStride * (crop.top shr shift) + pixelStride * (crop.left shr shift))
        for (row in 0 until heightShifted) {
            val length: Int
            if (pixelStride == 1 && outputStride == 1) {
                length = widthShifted
                buffer[output.array(), channelOffset, length]
                channelOffset += length
            } else {
                length = (widthShifted - 1) * pixelStride + 1
                buffer[rowData, 0, length]
                for (col in 0 until widthShifted) {
                    output.array()[channelOffset] = rowData[col * pixelStride]
                    channelOffset += outputStride
                }
            }
            if (row < heightShifted - 1) {
                buffer.position(buffer.position() + rowStride - length)
            }
        }
    }
    return output
}