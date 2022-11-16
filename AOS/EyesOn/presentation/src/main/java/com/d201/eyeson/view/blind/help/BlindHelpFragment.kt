package com.d201.eyeson.view.blind.help

import android.annotation.SuppressLint
import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentBlindHelpBinding
import com.d201.eyeson.util.OPENVIDU_URL
import com.d201.eyeson.view.blind.BlindHelpDisconnectListener
import com.d201.webrtc.openvidu.LocalParticipant
import com.d201.webrtc.openvidu.Session
import com.d201.webrtc.utils.CustomHttpClient
import com.d201.webrtc.utils.ParticipantListener
import com.d201.webrtc.websocket.CustomWebSocket
import dagger.hilt.android.AndroidEntryPoint
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

private const val TAG = "BlindHelpFragment"
@AndroidEntryPoint
class BlindHelpFragment : BaseFragment<FragmentBlindHelpBinding>(R.layout.fragment_blind_help) {

    private val blindHelpViewModel: BlindHelpViewModel by activityViewModels()

    @Inject
    lateinit var httpClient: CustomHttpClient

    private lateinit var audioManager: AudioManager
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var session: Session
    private lateinit var participantListener: ParticipantListener
    private lateinit var blindHelpDisconnectListener: BlindHelpDisconnectListener

    private var leaveFlag = false

    private var moveX = 0f
    private var moveY = 0f

    override fun init() {
        initView()
        initDevice()
        initListener()
        initWebRTC()
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start()
        mediaPlayer.isLooping = true
    }

    override fun onStop() {
        super.onStop()
        returnResource()
    }

    private fun initView() {
        binding.apply {
            lavLoading.playAnimation()
        }
    }

    private fun initDevice() {
        mediaPlayer = MediaPlayer.create(requireActivity(), R.raw.swans_in_flight)
        audioManager =
            requireActivity().getSystemService(AUDIO_SERVICE) as AudioManager
        audioManager.mode = AudioManager.MODE_NORMAL
        audioManager.isSpeakerphoneOn = true
        audioManager.isMicrophoneMute = false
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListener() {
        blindHelpDisconnectListener = object : BlindHelpDisconnectListener {
            override fun onClick() {
                requireActivity().finish()
            }
        }

        participantListener = object : ParticipantListener {
            override fun join() {
                Log.d(TAG, "ParticipantListener : join")
                lifecycleScope.launch {
                    if (mediaPlayer.isPlaying) {
                        mediaPlayer.stop()
                    }
                    binding.apply {
                        this@BlindHelpFragment.showToast("엔젤과 연결되었습니다.")
                        clLoading.visibility = View.GONE
                        lavLoading.pauseAnimation()
                        viewsContainer.bringToFront()
                        clMenu.bringToFront()
                        layoutTop.bringToFront()
                    }
                }
            }

            override fun left() {
                requireActivity().runOnUiThread {
                    Log.d(TAG, "ParticipantListener : left")
                    returnResource()
                    BlindHelpDisconnectDialog(blindHelpDisconnectListener).show(
                        parentFragmentManager,
                        "BlindHelpDisconnectDialog"
                    )
                }
            }
        }

        binding.apply {
            peerContainer.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        moveX = v.x - event.rawX
                        moveY = v.y - event.rawY
                    }

                    MotionEvent.ACTION_MOVE -> {
                        v.animate()
                            .x(event.rawX + moveX)
                            .y(event.rawY + moveY)
                            .setDuration(0)
                            .start()
                    }
                }

                true
            }
            ivCamera.setOnClickListener {
                showToast("카메라")
            }
            ivCameraSwitch.setOnClickListener {
                session.getLocalParticipant()!!.switchCamera()
                showToast("카메라 전환")
            }
            ivDisconnect.setOnClickListener {
                returnResource()

                BlindHelpDisconnectDialog(blindHelpDisconnectListener).show(
                    parentFragmentManager, "BlindHelpDisconnectDialog"
                )
            }
            ivSoundMode.setOnClickListener {
                // 소리 모드 변경
                audioManager.isSpeakerphoneOn = !audioManager.isSpeakerphoneOn
                when (audioManager.isSpeakerphoneOn) {
                    true -> {
                        ivSoundMode.setImageResource(R.drawable.btn_speaker_on)
                        showToast("스피커 모드")
                    }
                    false -> {
                        ivSoundMode.setImageResource(R.drawable.btn_speaker_off)
                        showToast("통화 모드")
                    }
                }

            }
            ivMic.setOnClickListener {
                // 마이크 OnOff
                audioManager.isMicrophoneMute = !audioManager.isMicrophoneMute
                when (audioManager.isMicrophoneMute) {
                    true -> {
                        ivMic.setImageResource(R.drawable.btn_mic_off)
                        showToast("마이크 OFF")
                    }
                    false -> {
                        ivMic.setImageResource(R.drawable.btn_mic)
                        showToast("마이크 ON")
                    }
                }

            }
        }
    }

    private fun initWebRTC() {
        initSurfaceView()
        getToken("${blindHelpViewModel.sessionId.value}-session")
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
                        Log.d(TAG, "responseString: " + response.body!!.string())

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
                                        Log.e(TAG, "Error getting body", e)
                                    }
                                    Log.d(TAG, "responseString2: $responseString")
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
                                    Log.e(TAG, "Error POST /api/tokens", e)
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
        binding.localGlSurfaceView.setMirror(false)
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
        localParticipant.startCamera("Blind")

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
            CustomWebSocket(
                session,
                OPENVIDU_URL,
                requireActivity() as AppCompatActivity,
                participantListener
            )
        webSocket.execute()
        session.setWebSocket(webSocket)
    }

    private fun returnResource() {
        Log.d(TAG, "returnResource: $leaveFlag")
        if(!leaveFlag) {
            leaveFlag = true
            mediaPlayer.stop()
            binding.lavLoading.pauseAnimation()
            leaveSession()
        }
    }

    private fun leaveSession() {
        if (::session.isInitialized) {
            this.session.leaveSession()
        }
        this.httpClient.dispose()
        requireActivity().runOnUiThread {
            binding.localGlSurfaceView.clearImage()
            binding.localGlSurfaceView.release()
            Log.d(TAG, "leaveSession: clearImage")
            binding.remoteGlSurfaceView.clearImage()
            binding.remoteGlSurfaceView.release()
        }

    }

}