package com.d201.eyeson.view.angel.help

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentAngelHelpBinding
import com.d201.eyeson.util.OPENVIDU_URL
import com.d201.webrtc.openvidu.LocalParticipant
import com.d201.webrtc.openvidu.Session
import com.d201.webrtc.utils.CustomHttpClient
import com.d201.webrtc.websocket.CustomWebSocket
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import org.webrtc.EglBase
import java.io.IOException
import javax.inject.Inject

private const val TAG = "AngelHelpFragment"

@AndroidEntryPoint
class AngelHelpFragment : BaseFragment<FragmentAngelHelpBinding>(R.layout.fragment_angel_help) {

    private val angelHelpViewModel: AngelHelpViewModel by viewModels()

    @Inject
    lateinit var httpClient: CustomHttpClient

    private lateinit var session: Session
    private lateinit var audioManager: AudioManager

    override fun init() {

        initWebRTC()
        initListener()
        initViewModelCallback()
        getSessionId()
    }

    override fun onStop() {
        super.onStop()
        leaveSession()
    }


    private fun initListener() {
        binding.apply {
            btnChangeCamera.setOnClickListener {
                session.getLocalParticipant()!!.switchCamera()
            }
            btnDisconnect.setOnClickListener {
                requireActivity().finish()
            }
        }
    }

    private fun initViewModelCallback() {
        lifecycleScope.launch {
            angelHelpViewModel.sessionId.collectLatest {
                if(it > 0)
                getToken("${angelHelpViewModel.sessionId.value}-session")
                Log.d(TAG, "initViewModelCallback: $it-session")

            }
        }
    }

    private fun getSessionId() {
        angelHelpViewModel.responseHelp()
    }

    private fun initWebRTC() {
        initSurfaceView()
        audioManager = requireActivity().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.mode = AudioManager.MODE_NORMAL
    }

    private fun getToken(sessionId: String) {
        try {
            // Session Request
            val sessionBody: RequestBody = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                "{\"customSessionId\": \"$sessionId\"}"
            )
            httpClient.httpCall(
                "/openvidu/api/sessions",
                "POST",
                "application/json",
                sessionBody,
                object : Callback {
                    @Throws(IOException::class)
                    override fun onResponse(call: Call, response: Response) {
                        Log.d(
                            TAG,
                            "responseString: " + response.body!!.string()
                        )

                        // Token Request
                        val tokenBody: RequestBody =
                            RequestBody.create(
                                "application/json; charset=utf-8".toMediaTypeOrNull(),
                                "{}"
                            )
                        httpClient.httpCall(
                            "/openvidu/api/sessions/$sessionId/connection",
                            "POST",
                            "application/json",
                            tokenBody,
                            object : Callback {
                                override fun onResponse(call: Call, response: Response) {
                                    var responseString: String? = null
                                    try {
                                        responseString = response.body!!.string()
                                    } catch (e: IOException) {
                                        Log.e(
                                            TAG,
                                            "Error getting body",
                                            e
                                        )
                                    }
                                    Log.d(
                                        TAG,
                                        "responseString2: $responseString"
                                    )
                                    var tokenJsonObject: JSONObject? = null
                                    var token: String? = null
                                    try {
                                        tokenJsonObject = JSONObject(responseString)
                                        token = tokenJsonObject.getString("token")
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                    getTokenSuccess(token!!, sessionId)
                                }

                                override fun onFailure(call: Call, e: IOException) {
                                    Log.e(
                                        TAG,
                                        "Error POST /api/tokens",
                                        e
                                    )
                                    viewToDisconnectedState()
                                }
                            })
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        Log.e(TAG, "Error POST /api/sessions", e)
                        viewToDisconnectedState()
                    }
                })
        } catch (e: IOException) {
            Log.e(TAG, "Error getting token", e)
            e.printStackTrace()
            viewToDisconnectedState()
        }
    }

    private fun initSurfaceView() {
        val rootEgleBase = EglBase.create()
        binding.localGlSurfaceView.init(rootEgleBase.eglBaseContext, null)
        binding.localGlSurfaceView.setMirror(true)
        binding.localGlSurfaceView.setEnableHardwareScaler(true)
        binding.localGlSurfaceView.setZOrderMediaOverlay(true)
    }

    private fun getTokenSuccess(token: String, sessionId: String) {
        // Initialize our session
        session = Session(
            sessionId,
            token,
            requireActivity() as AppCompatActivity,
            binding.viewsContainer
        )

        // Initialize our local participant and start local camera
        val participantName: String = "customerName"
        val localParticipant =
            LocalParticipant(
                participantName,
                session,
                requireActivity().applicationContext,
                binding.localGlSurfaceView
            )
        localParticipant.startCamera()

        // Initialize and connect the websocket to OpenVidu Server
        startWebSocket()
    }

    fun viewToDisconnectedState() {
        requireActivity().runOnUiThread {
            binding.apply {
                localGlSurfaceView.clearImage()
                localGlSurfaceView.release()
            }
        }
    }

    private fun startWebSocket() {
        val webSocket =
            CustomWebSocket(session, OPENVIDU_URL, requireActivity() as AppCompatActivity)
        webSocket.execute()
        session.setWebSocket(webSocket)
    }

    private fun leaveSession() {
        angelHelpViewModel.disconnectHelp()
        if(::session.isInitialized) {
            this.session.leaveSession()
        }
        this.httpClient.dispose()
        requireActivity().runOnUiThread {
            binding.localGlSurfaceView.clearImage()
            binding.localGlSurfaceView.release()
            binding.remoteGlSurfaceView.clearImage()
            binding.remoteGlSurfaceView.release()
        }

    }
}