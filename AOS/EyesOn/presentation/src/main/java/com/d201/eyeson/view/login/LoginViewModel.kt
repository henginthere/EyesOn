package com.d201.eyeson.view.login


import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d201.domain.model.Login
import com.d201.domain.usecase.LoginUseCase
import com.d201.domain.utils.ResultType
import com.d201.eyeson.util.JWT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LoginViewModel"
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase, private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _login: MutableStateFlow<Login?> = MutableStateFlow(null)
    val login get() = _login.asStateFlow()

    fun login(idToken: String, fcmToken: String){
        viewModelScope.launch(Dispatchers.IO) {
            loginUseCase.execute(idToken, fcmToken).collectLatest {
                if(it is ResultType.Success && it.data.status == 200){
                    // 로그인 성공 처리
                    _login.value = it.data.data
                    sharedPreferences.edit().putString(JWT, it.data.data.jwtToken.accessToken).apply()
                    Log.d(TAG, "login: ${sharedPreferences.getString(JWT, "null")}")
                } else {
                    //로그인 실패
                    Log.d(TAG, "login: ${it}")
                }
            }
        }
    }
}