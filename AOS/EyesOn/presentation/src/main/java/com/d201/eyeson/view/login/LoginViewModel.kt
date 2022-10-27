package com.d201.eyeson.view.login


import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    //private val loginUseCase: LoginUseCase
) : ViewModel() {

    fun firebaseAuthWithGoogle(token: String) {

    }
}