package com.d201.eyeson.view.login.login

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentLoginBinding
import com.d201.eyeson.util.ANGEL
import com.d201.eyeson.util.BLIND
import com.d201.eyeson.util.GENDER_DEFAULT
import com.d201.eyeson.util.JWT
import com.d201.eyeson.view.angel.main.AngelMainActivity
import com.d201.eyeson.view.blind.BlindMainActivity
import com.d201.eyeson.view.login.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LoginFragment"

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private var action = ""
    private var fcmToken = ""

    override fun init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel("EyesOn_id", "EyesOn")
        }
        if (sharedPreferences.getString(JWT, "null") == "null"){
            PermissionDialog().show(parentFragmentManager, "PermissionDialog")
        }
        initAction()
        initFirebaseTokenListener()
        initListener()
        initViewModelCallback()
    }

    private fun initAction() {
        val intent = requireActivity().intent
        val extra = intent.extras
        if (intent != null && extra?.getString("action") != null) {
            action = extra.getString("action")!!
        }
    }

    private fun initFirebaseTokenListener() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener {
            if (it.isSuccessful) {
                Log.d(TAG, "FCM Token: ${it.result}")
                fcmToken = it.result
            } else {
                Log.d(TAG, "FCM 토큰 얻기 실패", it.exception)
            }
        })
    }

    private fun initViewModelCallback() {
        lifecycleScope.launch {
            loginViewModel.login.collectLatest {
                when (it?.gender) {
                    GENDER_DEFAULT -> {
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSelectRoleFragment())
                    }
                    else -> {
                        when (it?.role) {
                            BLIND -> startActivity(
                                Intent(
                                    requireContext(),
                                    BlindMainActivity::class.java
                                ).apply {
                                    putExtra("Gender", it.gender)
                                }
                            )
                            ANGEL -> {
                                val angelMainIntent =
                                    Intent(requireContext(), AngelMainActivity::class.java).apply {
                                        putExtra("Gender", it.gender)
                                        putExtra("action", this@LoginFragment.action)
                                    }

                                startActivity(angelMainIntent)
                            }
                            else -> return@collectLatest
                        }
                        requireActivity().finish()
                    }
                }
            }
        }
    }

    private fun initListener() {
        binding.apply {
            sbLogin.setOnClickListener {
                initAuth()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance)

        val notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }


    private fun initAuth() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            // default_web_client_id 값은 build 타임에 values.xml 파일에 자동 생성
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail() // 인증 방식: gmail
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        // Google에서 제공되는 signInIntent를 이용해서 인증 시도
        val signInIntent = mGoogleSignInClient.signInIntent

        //콜백함수 부르며 launch
        requestActivity.launch(signInIntent)
    }

    // 구글 인증 결과 획득 후 동작 처리
    private val requestActivity: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        Log.d(
            TAG,
            "GoogleLoginActivityResult : ${activityResult.resultCode}, RESULT_OK : ${AppCompatActivity.RESULT_OK}"
        )
        if (activityResult.resultCode == Activity.RESULT_OK) {

            // 구글 인증 결과 획득
            val task = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)

            try {
                val account = task.getResult(ApiException::class.java)

                Log.d(TAG, "Id Token: ${account.idToken}")

                // 발급 받은 IdToken 과 FcmToken 을 서버로 전송
                loginViewModel.login(account.idToken!!, fcmToken)

            } catch (e: ApiException) {
                Log.e(TAG, "google sign in failed: ", e)
            }
        }
    }

}