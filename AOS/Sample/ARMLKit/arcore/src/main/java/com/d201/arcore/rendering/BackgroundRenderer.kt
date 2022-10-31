package com.practice.depth.depth.rendering

import android.content.Context
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.util.Log
import com.d201.arcore.rendering.ShaderUtil
import com.google.ar.core.Coordinates2d
import com.google.ar.core.Frame
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class BackgroundRenderer {
    private val TAG = BackgroundRenderer::class.java.simpleName

    // Shader names.
    private val CAMERA_VERTEX_SHADER_NAME = "shaders/screenquad.vert"
    private val CAMERA_FRAGMENT_SHADER_NAME = "shaders/screenquad.frag"
    private val DEPTH_VERTEX_SHADER_NAME = "shaders/background_show_depth_map.vert"
    private val DEPTH_FRAGMENT_SHADER_NAME = "shaders/background_show_depth_map.frag"


    private val COORDS_PER_VERTEX = 2
    private val TEXCOORDS_PER_VERTEX = 2
    private val FLOAT_SIZE = 4

    private lateinit var quadCoords: FloatBuffer
    private var quadTexCoords: FloatBuffer? = null

    private var quadProgram = 0

    private var quadPositionParam = 0
    private var quadTexCoordParam = 0
    private var textureId = -1

    private var depthProgram = 0
    private var depthTextureParam = 0
    private var depthTextureId = -1
    private var depthQuadPositionParam = 0
    private var depthQuadTexCoordParam = 0

    private val MAX_DEPTH_RANGE_TO_RENDER_MM = 20000.0f
    private var depthRangeToRenderMm = 0.0f
    private var depthRangeToRenderMmParam = 0

    fun getTextureId(): Int {
        return textureId
    }

    /**
     * Allocates and initializes OpenGL resources needed by the background renderer. Must be called on
     * the OpenGL thread, typically in [GLSurfaceView.Renderer.onSurfaceCreated].
     *
     * @param context Needed to access shader source.
     */
    @Throws(IOException::class)
    fun createOnGlThread(context: Context?) {
        // Generate the background texture.
        Log.d(TAG, "createOnGlThread: $context")
        val textures = IntArray(1)
        GLES20.glGenTextures(1, textures, 0)
        textureId = textures[0]
        val textureTarget = GLES11Ext.GL_TEXTURE_EXTERNAL_OES
        GLES20.glBindTexture(textureTarget, textureId)
        GLES20.glTexParameteri(textureTarget, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(textureTarget, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(textureTarget, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(textureTarget, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        val numVertices = 4
        if (numVertices != QUAD_COORDS.size / COORDS_PER_VERTEX) {
            throw RuntimeException("Unexpected number of vertices in BackgroundRenderer.")
        }
        val bbCoords = ByteBuffer.allocateDirect(QUAD_COORDS.size * FLOAT_SIZE)
        bbCoords.order(ByteOrder.nativeOrder())
        quadCoords = bbCoords.asFloatBuffer()
        quadCoords.put(QUAD_COORDS)
        quadCoords.position(0)
        val bbTexCoordsTransformed =
            ByteBuffer.allocateDirect(numVertices * TEXCOORDS_PER_VERTEX * FLOAT_SIZE)
        bbTexCoordsTransformed.order(ByteOrder.nativeOrder())
        quadTexCoords = bbTexCoordsTransformed.asFloatBuffer()
        val vertexShader: Int = ShaderUtil.loadGLShader(
            TAG,
            context!!,
            GLES20.GL_VERTEX_SHADER,
            CAMERA_VERTEX_SHADER_NAME
        )
        val fragmentShader: Int = ShaderUtil.loadGLShader(
            TAG,
            context,
            GLES20.GL_FRAGMENT_SHADER,
            CAMERA_FRAGMENT_SHADER_NAME
        )
        quadProgram = GLES20.glCreateProgram()
        GLES20.glAttachShader(quadProgram, vertexShader)
        GLES20.glAttachShader(quadProgram, fragmentShader)
        GLES20.glLinkProgram(quadProgram)
        GLES20.glUseProgram(quadProgram)
        ShaderUtil.checkGLError(TAG, "Program creation")
        quadPositionParam = GLES20.glGetAttribLocation(quadProgram, "a_Position")
        quadTexCoordParam = GLES20.glGetAttribLocation(quadProgram, "a_TexCoord")
        ShaderUtil.checkGLError(TAG, "Program parameters")
    }

    @Throws(IOException::class)
    fun createDepthShaders(context: Context?, depthTextureId: Int) {
        // Loads shader for rendering depth map.
        val vertexShader: Int = ShaderUtil.loadGLShader(
            TAG, context!!, GLES20.GL_VERTEX_SHADER, DEPTH_VERTEX_SHADER_NAME
        )
        val fragmentShader: Int = ShaderUtil.loadGLShader(
            TAG, context, GLES20.GL_FRAGMENT_SHADER, DEPTH_FRAGMENT_SHADER_NAME
        )
        depthProgram = GLES20.glCreateProgram()
        GLES20.glAttachShader(depthProgram, vertexShader)
        GLES20.glAttachShader(depthProgram, fragmentShader)
        GLES20.glLinkProgram(depthProgram)
        GLES20.glUseProgram(depthProgram)
        ShaderUtil.checkGLError(TAG, "Program creation")
        depthTextureParam = GLES20.glGetUniformLocation(depthProgram, "u_Depth")
        depthRangeToRenderMmParam =
            GLES20.glGetUniformLocation(depthProgram, "u_DepthRangeToRenderMm")
        ShaderUtil.checkGLError(TAG, "Program parameters")
        depthQuadPositionParam = GLES20.glGetAttribLocation(depthProgram, "a_Position")
        depthQuadTexCoordParam = GLES20.glGetAttribLocation(depthProgram, "a_TexCoord")
        this.depthTextureId = depthTextureId
    }

    /**
     * Draws the AR background image. The image will be drawn such that virtual content rendered with
     * the matrices provided by [com.google.ar.core.Camera.getViewMatrix] and
     * [com.google.ar.core.Camera.getProjectionMatrix] will
     * accurately follow static physical objects. This must be called **before** drawing virtual
     * content.
     *
     * @param frame The current `Frame` as returned by [Session.update].
     */
    fun draw(frame: Frame) {
        // If display rotation changed (also includes view size change), we need to re-query the uv
        // coordinates for the screen rect, as they may have changed as well.
        if (frame.hasDisplayGeometryChanged()) {
            frame.transformCoordinates2d(
                Coordinates2d.OPENGL_NORMALIZED_DEVICE_COORDINATES,
                quadCoords,
                Coordinates2d.TEXTURE_NORMALIZED,
                quadTexCoords
            )
        }
        if (frame.timestamp == 0L) {
            // Suppress rendering if the camera did not produce the first frame yet. This is to avoid
            // drawing possible leftover data from previous sessions if the texture is reused.
            return
        }
        draw()
    }

    /**
     * Draws the camera background image using the currently configured [ ][BackgroundRenderer.quadTexCoords] image texture coordinates.
     */
    private fun draw() {
        // Ensure position is rewound before use.
        quadTexCoords!!.position(0)

        // No need to test or write depth, the screen quad has arbitrary depth, and is expected
        // to be drawn first.
        GLES20.glDisable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthMask(false)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId)
        GLES20.glUseProgram(quadProgram)

        // Set the vertex positions.
        GLES20.glVertexAttribPointer(
            quadPositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, quadCoords
        )

        // Set the texture coordinates.
        GLES20.glVertexAttribPointer(
            quadTexCoordParam, TEXCOORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, quadTexCoords
        )

        // Enable vertex arrays
        GLES20.glEnableVertexAttribArray(quadPositionParam)
        GLES20.glEnableVertexAttribArray(quadTexCoordParam)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        // Disable vertex arrays
        GLES20.glDisableVertexAttribArray(quadPositionParam)
        GLES20.glDisableVertexAttribArray(quadTexCoordParam)

        // Restore the depth state for further drawing.
        GLES20.glDepthMask(true)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        ShaderUtil.checkGLError(TAG, "BackgroundRendererDraw")
    }

    fun drawDepth(frame: Frame) {
        if (frame.hasDisplayGeometryChanged()) {
            frame.transformCoordinates2d(
                Coordinates2d.OPENGL_NORMALIZED_DEVICE_COORDINATES,
                quadCoords,
                Coordinates2d.TEXTURE_NORMALIZED,
                quadTexCoords
            )
        }
        if (frame.timestamp == 0L || depthTextureId == -1) {
            return
        }

        // Ensure position is rewound before use.
        quadTexCoords!!.position(0)

        // No need to test or write depth, the screen quad has arbitrary depth, and is expected
        // to be drawn first.
        GLES20.glDisable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthMask(false)
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, depthTextureId)
        GLES20.glUseProgram(depthProgram)
        GLES20.glUniform1i(depthTextureParam, 0)
        depthRangeToRenderMm += 50.0f
        if (depthRangeToRenderMm > MAX_DEPTH_RANGE_TO_RENDER_MM) {
            depthRangeToRenderMm = 0.0f
        }
        GLES20.glUniform1f(depthRangeToRenderMmParam, depthRangeToRenderMm)

        // Set the vertex positions and texture coordinates.
        GLES20.glVertexAttribPointer(
            depthQuadPositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, quadCoords
        )
        GLES20.glVertexAttribPointer(
            depthQuadTexCoordParam, TEXCOORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, quadTexCoords
        )

        // Draws the quad.
        GLES20.glEnableVertexAttribArray(depthQuadPositionParam)
        GLES20.glEnableVertexAttribArray(depthQuadTexCoordParam)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        GLES20.glDisableVertexAttribArray(depthQuadPositionParam)
        GLES20.glDisableVertexAttribArray(depthQuadTexCoordParam)

        // Restore the depth state for further drawing.
        GLES20.glDepthMask(true)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        ShaderUtil.checkGLError(TAG, "BackgroundRendererDraw")
    }

    /**
     * (-1, 1) ------- (1, 1)
     * |    \           |
     * |       \        |
     * |          \     |
     * |             \  |
     * (-1, -1) ------ (1, -1)
     * Ensure triangles are front-facing, to support glCullFace().
     * This quad will be drawn using GL_TRIANGLE_STRIP which draws two
     * triangles: v0->v1->v2, then v2->v1->v3.
     */
    private var QUAD_COORDS = floatArrayOf(
        -1.0f, -1.0f, +1.0f, -1.0f, -1.0f, +1.0f, +1.0f, +1.0f
    )
}