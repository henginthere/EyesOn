package com.d201.eyeson.util

import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

private const val TAG = "XAccessTokenInterceptor"

class XAccessTokenInterceptor @Inject constructor(
    private val sharedPref: SharedPreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

//        var token = CoroutineScope(Dispatchers.Main).launch{
//            sharedPref.getString(JWT,"")!!
//        }
        var token = runBlocking {
            sharedPref.getString(JWT, "")!!
        }
        val request = chain.request().newBuilder()
            .addHeader(JWT, "Bearer $token")
            .build()
        Log.d(TAG, "intercept headers: ${request.headers} ")
        Log.d(TAG, "intercept body : ${request.body} ")
        return chain.proceed(request)
    }
}