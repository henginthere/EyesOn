package com.d201.eyeson.view.login


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d201.domain.base.BaseResponse
import com.d201.domain.model.User
import com.d201.domain.usecase.LoginUseCase
import com.d201.domain.utils.ResultType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _user: MutableStateFlow<ResultType<BaseResponse<User>>> =
        MutableStateFlow(ResultType.Uninitialized)
    val user get() = _user.asStateFlow()

    fun login(idToken: String, fcmToken: String){
        viewModelScope.launch(Dispatchers.IO) {
            loginUseCase.execute(idToken, fcmToken).collectLatest {
                if(it is ResultType.Success){
                    // 로그인 성공 처리
                    _user.value = it
                } else {
                    //로그인 실패
                }
            }
        }
    }
}