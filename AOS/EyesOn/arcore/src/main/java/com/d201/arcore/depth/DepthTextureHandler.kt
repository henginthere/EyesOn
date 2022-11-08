package com.d201.depth.depth

import android.content.Context
import android.media.Image
import android.opengl.GLES20
import android.opengl.GLES30
import android.util.Log
import com.google.ar.core.Coordinates2d
import com.google.ar.core.Frame
import com.google.ar.core.exceptions.NotYetAvailableException
import java.nio.ByteOrder
private const val TAG = "DepthTextureHandler"
class DepthTextureHandler(var context: Context) {
    private var depthTextureId = -1
    private var depthTextureWidth = -1
    private var depthTextureHeight = -1
  var distance1 = 0
  var distance2 = 0
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
     * AcquireDepthImage()의 내용으로 깊이 텍스처를 업데이트합니다.
     * 이 메소드는 EGL 컨텍스트가 연결된 스레드에서 호출해야 합니다.
     *
     */

    fun update(frame: Frame) {
        try {
//            val originImage = frame.acquireCameraImage()
            val depthImage = frame.acquireDepthImage16Bits()
//            val rawDepthImage = frame.acquireRawDepthImage16Bits()

           // Log.d(TAG, "originImage : ${originImage.width}  ${originImage.height}" )
            Log.d(TAG, "originImage : ${depthImage.width}  ${depthImage.height}" )
//            Log.d(TAG, "originImage : ${rawDepthImage.width}  ${rawDepthImage.height}" )
            // 160*120 픽셀
            depthTextureWidth = depthImage.width
            depthTextureHeight = depthImage.height
            Log.d(TAG, "depthTexturepixelStride : ${depthImage.planes[0].pixelStride}" )
            Log.d(TAG, "depthTexturerowStride : ${depthImage.planes[0].rowStride}" )
            Log.d(TAG, "depthTexturebuffer : ${depthImage.planes[0].buffer}" )
            Log.d(TAG, "depthTextureWidth : $depthTextureWidth" )
            Log.d(TAG, "depthTextureHeight : $depthTextureHeight" )
            Log.d(TAG, "depthTexturePixelStride : ${(depthTextureWidth*depthImage.planes[0].pixelStride)} : ${(depthTextureHeight*depthImage.planes[0].pixelStride)}" )
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
//            distance1 = getMillimetersDepth(depthImage, (depthTextureWidth*depthImage.planes[0].rowStride), (depthTextureHeight*depthImage.planes[0].rowStride))
//            distance2 = getMillimetersDepth(depthImage, (depthTextureWidth*depthImage.planes[0].pixelStride), (depthTextureHeight*depthImage.planes[0].pixelStride))
//            onDepthImageUpdateListener.onUpdateDepthImage(distance)
            depthImage.close()
        } catch (e: NotYetAvailableException) {
            // This normally means that depth data is not available yet.
        }
    }


    fun getMillimetersDepth(depthImage: Image, x: Int, y: Int): Int {
        // The depth image has a single plane, which stores depth for each pixel as 16-bit unsigned integers.
        // 깊이 이미지에는 각 픽셀의 깊이를 16비트 부호 없는 정수로 저장하는 단일 평면이 있습니다.
        val plane = depthImage.planes[0]
        val byteIndex = x * plane.pixelStride + y * plane.rowStride
        val buffer = plane.buffer.order(ByteOrder.nativeOrder())
        val depthSample = buffer.getShort(byteIndex)
        Log.d(TAG, "depthTexture byteIndex : $byteIndex" )
        Log.d(TAG, "depthTexture buffer: $buffer" )
        Log.d(TAG, "depthTexture depthSample: $depthSample" )
        return depthSample.toInt()
    }

    fun pointConverter(frame: Frame, depthImage: Image,cpuCoordinateX: Int,cpuCoordinateY: Int): Pair<Int, Int>? {
        val cpuCoordinates = floatArrayOf(cpuCoordinateX.toFloat(), cpuCoordinateY.toFloat())
        val textureCoordinates = FloatArray(2)
        frame.transformCoordinates2d(
            Coordinates2d.IMAGE_PIXELS,
            cpuCoordinates,
            Coordinates2d.TEXTURE_NORMALIZED,
            textureCoordinates
        )
        if (textureCoordinates[0] < 0 || textureCoordinates[1] < 0) {
            // CPU 이미지의 좌표가 깊이 이미지의 잘린 영역에 있기 때문에 유효한 깊이 좌표가 없습니다.
            return null
        }
        return (textureCoordinates[0] * depthImage.width).toInt() to
                (textureCoordinates[1] * depthImage.height).toInt()
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