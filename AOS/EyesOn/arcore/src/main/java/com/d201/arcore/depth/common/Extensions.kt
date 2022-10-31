package com.d201.arcore.depth.common

import android.app.Activity
import android.content.Context
import android.graphics.Insets
import android.graphics.Point
import android.view.WindowInsets
import android.view.WindowManager

const val OBJECT_DETECTION = "Object Detection"
const val OBJECT_DETECTION_CUSTOM = "Custom Object Detection"
const val CUSTOM_AUTOML_OBJECT_DETECTION = "Custom AutoML Object Detection (Flower)"
const val TEXT_RECOGNITION_KOREAN = "Text Recognition Korean (Beta)"


const val FACE_DETECTION = "Face Detection"
const val TEXT_RECOGNITION_LATIN = "Text Recognition Latin"
const val TEXT_RECOGNITION_CHINESE = "Text Recognition Chinese (Beta)"
const val TEXT_RECOGNITION_DEVANAGARI = "Text Recognition Devanagari (Beta)"
const val TEXT_RECOGNITION_JAPANESE = "Text Recognition Japanese (Beta)"
const val BARCODE_SCANNING = "Barcode Scanning"
const val IMAGE_LABELING = "Image Labeling"
const val IMAGE_LABELING_CUSTOM = "Custom Image Labeling (Birds)"
const val CUSTOM_AUTOML_LABELING = "Custom AutoML Image Labeling (Flower)"
const val POSE_DETECTION = "Pose Detection"
const val SELFIE_SEGMENTATION = "Selfie Segmentation"
const val FACE_MESH_DETECTION = "Face Mesh Detection (Beta)";

const val STATE_SELECTED_MODEL = "selected_model"

fun getDeviceSize(activity: Activity): Point {
    val windowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return windowManager.currentWindowMetricsPointCompat()
}

fun WindowManager.currentWindowMetricsPointCompat() : Point {
    // R(30) 이상
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
        val windowInsets = currentWindowMetrics.windowInsets
        var insets: Insets = windowInsets.getInsets(WindowInsets.Type.navigationBars())
        windowInsets.displayCutout?.run {
            insets = Insets.max(
                insets,
                Insets.of(safeInsetLeft, safeInsetTop, safeInsetRight, safeInsetBottom)
            )
        }
        val insetsWidth = insets.right + insets.left
        val insetsHeight = insets.top + insets.bottom
        Point(
            currentWindowMetrics.bounds.width() - insetsWidth,
            currentWindowMetrics.bounds.height() - insetsHeight
        )
    } else {
        Point().apply {
            defaultDisplay.getSize(this)
        }
    }
}