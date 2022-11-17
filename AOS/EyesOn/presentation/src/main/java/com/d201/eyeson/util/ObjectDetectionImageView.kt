package com.d201.eyeson.util

import android.content.Context
import android.graphics.*
import android.media.Image
import androidx.appcompat.widget.AppCompatImageView
import com.d201.eyeson.view.blind.findobject.DetectionResult

class ObjectDetectionImageView(context: Context?) : AppCompatImageView(context!!) {
    private var latest: Bitmap? = null

    fun setDetectionResult(detectionResults: List<DetectionResult>?, bitmap: Bitmap, depthImage: Image?, distance: Int){
        if(detectionResults == null) return
        if(depthImage == null) return
        val pen = Paint()
        latest = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val canvas = Canvas(latest!!)

        detectionResults.forEach {
            if (it.boundingBox.width() < 1000) {
                // 박스 그리기
                pen.color = Color.RED
                pen.strokeWidth = 8F
                pen.style = Paint.Style.STROKE
                val box = it.boundingBox
                canvas.drawRect(box, pen)

                // 중점 그리기
                canvas.drawCircle(it.boundingBox.centerX(), it.boundingBox.centerY(), 8F, pen)

                // 객체 이름, 점수 그리기
                pen.style = Paint.Style.FILL_AND_STROKE
                pen.color = Color.YELLOW
                pen.strokeWidth = 2F
                pen.textAlign = Paint.Align.LEFT
                pen.textSize = MAX_FONT_SIZE

                val objectText = "${it.text} ${it.score}%"

                val tagSize = Rect(0, 0, 0, 0)
                pen.getTextBounds(objectText, 0, objectText.length, tagSize)
                val fontSize: Float = pen.textSize * box.width() / tagSize.width()

                // adjust the font size so texts are inside the bounding box
                if (fontSize < pen.textSize) pen.textSize = fontSize

                var margin = (box.width() - tagSize.width()) / 2.0F
                if (margin < 0F) margin = 0F
                canvas.drawText(
                    objectText, box.left + margin,
                    box.top + tagSize.height().times(1F), pen
                )

                // 객체와의 거리 그리기
                val centerX = it.boundingBox.centerX().toInt()
                val centerY = it.boundingBox.centerY().toInt()

                val ratio = bitmap.height / depthImage.width
                val depthX = centerY / ratio
                val depthY = depthImage.height - (centerX / ratio)

                var cm = (distance / 10.0).toFloat()
                var convertedDistance = ""
                if (cm > 100) {
                    cm /= 100
                    convertedDistance = "%.1f m".format(cm)
                } else {
                    convertedDistance = "${cm.toInt()} cm"
                }
                canvas.drawText(
                    convertedDistance, box.centerX(),
                    box.centerY(), pen
                )

                var location = ""
                val w = bitmap.width / 3
                val h = bitmap.height / 3

                if (centerX in 0..w) {
                    if (centerY in 0..h) {
                        location = "11시 방향"
                    } else if (centerY in h..h * 2) {
                        location = "9시 방향"
                    } else if (centerY in h * 2..bitmap.height) {
                        location = "7시 방향"
                    }
                } else if (centerX in w..w * 2) {
                    if (centerY in 0..h) {
                        location = "12시 방향"
                    } else if (centerY in h..h * 2) {
                        location = "중앙"
                    } else if (centerY in h * 2..bitmap.height) {
                        location = "6시 방향"
                    }
                } else if (centerX in w * 2..bitmap.width) {
                    if (centerY in 0..h) {
                        location = "1시 방향"
                    } else if (centerY in h..h * 2) {
                        location = "3시 방향"
                    } else if (centerY in h * 2..bitmap.height) {
                        location = "5시 방향"
                    }
                }
            }
        }
    }
    fun update(){
        postInvalidate()
        if(latest != null){
            setImageBitmap(latest)
        }
    }
}