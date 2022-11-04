package com.d201.arcore.depth

import android.content.Context
import android.media.Image
import android.opengl.GLES20
import android.opengl.GLES30
import android.util.Log
import com.google.ar.core.Frame
import com.google.ar.core.exceptions.NotYetAvailableException
import java.nio.ByteOrder

const val TAG = "DepthTextureHandler__"
class DepthTextureHandler(var context: Context) {
    private var depthTextureId = -1
    private var depthTextureWidth = -1
    private var depthTextureHeight = -1
    var distance:Int = 0

    /**
     * Creates and initializes the depth texture. This method needs to be called on a
     * thread with a EGL context attached.
     */
    fun createOnGlThread() {
        val textureId = IntArray(1)
        GLES20.glGenTextures(1, textureId, 0)
        depthTextureId = textureId[0]
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, depthTextureId)
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_WRAP_S,
            GLES20.GL_CLAMP_TO_EDGE
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_WRAP_T,
            GLES20.GL_CLAMP_TO_EDGE
        )
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
    }

    /**
     * Updates the depth texture with the content from acquireDepthImage().
     * This method needs to be called on a thread with a EGL context attached.
     */
    // centerX, centerY : MLKit의 객체 박스의 중간 지점
    fun update(frame: Frame, centerX: Float, centerY: Float) {
        try {
            var x = (centerX*2/9)
            var y = (centerY*2/9)
            Log.d(TAG, "x: $x, centerY$y, distance: ${distance}")
            val depthImage = frame.acquireDepthImage16Bits()
            Log.d(TAG, depthImage.width.toString())
            depthTextureWidth = depthImage.width
            depthTextureHeight = depthImage.height
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, depthTextureId)
            GLES20.glTexImage2D(
                GLES20.GL_TEXTURE_2D,
                0,
                GLES30.GL_RG8,
                depthTextureWidth,
                depthTextureHeight,
                0,
                GLES30.GL_RG,
                GLES20.GL_UNSIGNED_BYTE,
                depthImage.planes[0].buffer
            )

            // 좌표에서 depthImage의 깊이를 밀리미터 단위로 가져옵니다
            distance = getMillimetersDepth(depthImage, x.toInt(), y.toInt())
//            onDepthImageUpdateListener.onUpdateDepthImage(distance)
//            (context as MainActivity).onUpdateDepthImage(distance)
            depthImage.close()

        } catch (e: NotYetAvailableException) {
            // This normally means that depth data is not available yet.
        }
    }

    interface OnDepthImageUpdateListener{
        fun onUpdateDepthImage(distance: Int)
    }
    lateinit var onDepthImageUpdateListener: OnDepthImageUpdateListener

    fun getMillimetersDepth(depthImage: Image, x: Int, y: Int): Int {
        // The depth image has a single plane, which stores depth for each
        // pixel as 16-bit unsigned integers.
        val plane = depthImage.planes[0]
        val byteIndex = x * plane.pixelStride + y * plane.rowStride
        val buffer = plane.buffer.order(ByteOrder.nativeOrder())
        val depthSample = buffer.getShort(byteIndex)
        return depthSample.toInt()
    }

    fun getDepthTexture(): Int {
        return depthTextureId
    }

    fun getDepthWidth(): Int {
        return depthTextureWidth
    }

    fun getDepthHeight(): Int {
        return depthTextureHeight
    }
}