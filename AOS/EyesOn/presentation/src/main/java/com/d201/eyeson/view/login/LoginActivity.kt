package com.d201.eyeson.view.login

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.d201.eyeson.R
import com.d201.eyeson.databinding.ActivityLoginBinding
import com.d201.eyeson.util.TAG
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.ssafy.indive.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val loginViewModel : LoginViewModel by viewModels()

    private lateinit var mGoogleSignInClient : GoogleSignInClient

    override fun init() {
        initAuth()
    }

    private fun initAuth() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            // default_web_client_id 값은 build 타임에 values.xml 파일에 자동 생성
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail() // 인증 방식: gmail
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        //mAuth = FirebaseAuth.getInstance()

        // Google에서 제공되는 signInIntent를 이용해서 인증 시도
        val signInIntent = mGoogleSignInClient!!.signInIntent

        //콜백함수 부르며 launch
        requestActivity.launch(signInIntent)
    }

    // 구글 인증 결과 획득 후 동작 처리
    private val requestActivity: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        Log.d(
            TAG,
            "firebaseAuthWithGoogle: Activity.RESULT_OK): ${RESULT_OK}, activityResult.resultCode:${activityResult.resultCode}"
        )
        if (activityResult.resultCode == Activity.RESULT_OK) {

            // 인증 결과 획득
            val task = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)

            try {
                val account = task.getResult(ApiException::class.java)
                Log.d(TAG, "firebaseAuthWithGoogle: account: ${account.idToken}")
                //loginViewModel.firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w(TAG, "google sign in failed: ", e)
            }
        }
    }


}