package com.d201.eyeson.view.blind.setting

import android.content.Intent
import android.content.SharedPreferences
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentBlindSettingBinding
import com.d201.eyeson.util.JWT
import com.d201.eyeson.util.accessibilityEvent
import com.d201.eyeson.view.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


private const val TAG = "BlindSettingFragment"
@AndroidEntryPoint
class BlindSettingFragment : BaseFragment<FragmentBlindSettingBinding>(R.layout.fragment_blind_setting) {
    @Inject
    lateinit var sharedPref: SharedPreferences
    private val viewModel: BlindSettingViewModel by viewModels()

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun init() {
        initListener()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.deleteUserEvent.observe(viewLifecycleOwner){
            if(it){
                finishActivity()
            }
        }
    }

    private fun initListener() {
        binding.apply {
            btnBack.apply{
                setOnClickListener{
                    accessibilityDelegate = accessibilityEvent(this, requireContext())
                    setOnClickListener {
                        findNavController().popBackStack()
                    }
                }
            }

            btnReplayGuide.setOnClickListener {  }

            btnLogout.setOnClickListener {
                logOut()
            }

            btnSignOut.setOnClickListener {
                viewModel.deleteUser()
            }
        }
    }

    private fun finishActivity(){
        sharedPref.edit().remove(JWT).apply()
        requireActivity().startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }

    private fun logOut() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        mGoogleSignInClient.signOut().addOnCompleteListener {
            finishActivity()
        }
    }
}

