package com.d201.eyeson.view.login.login

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentLoginBinding
import com.d201.eyeson.view.login.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "LoginFragment"
@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {
    private val loginViewModel : LoginViewModel by viewModels()
    private lateinit var mGoogleSignInClient : GoogleSignInClient

    override fun init() {
        initListener()
        initAuth()
    }


    private fun initListener(){
        binding.apply {
            sbLogin.setOnClickListener {
                initAuth()
            }
        }
    }

    private fun initAuth() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            // default_web_client_id 값은 build 타임에 values.xml 파일에 자동 생성
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail() // 인증 방식: gmail
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

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
            "GoogleLoginActivityResult : ${activityResult.resultCode}, RESULT_OK : ${AppCompatActivity.RESULT_OK}"
        )
        if (activityResult.resultCode == Activity.RESULT_OK) {
            // 인증 결과 획득
            val task = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)

            try {
                val account = task.getResult(ApiException::class.java)
                Log.d(TAG, "IdToken: ${account.idToken}")
                loginViewModel.login(account.idToken!!, "i")
            } catch (e: ApiException) {
                Log.e(TAG, "google sign in failed: ", e)
            }
        }
    }

}