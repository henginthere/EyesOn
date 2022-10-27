package com.d201.eyeson.module

import android.provider.Settings.Global.getString
import com.d201.eyeson.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//@Module
//@InstallIn(SingletonComponent::class)
//class LoginModule {
//    // BeginSignInRequest DI
//    @Provides
//    @Singleton
//    fun provideBeginSignInRequestInstance(): BeginSignInRequest {
//        return BeginSignInRequest.builder()
//            .setGoogleIdTokenRequestOptions(
//                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                    .setSupported(true)
//                    // Your server's client ID, not your Android client ID.
//                    .setServerClientId(getString(R.string.your_web_client_id))
//                    // Only show accounts previously used to sign in.
//                    .setFilterByAuthorizedAccounts(true)
//                    .build()
//            )
//            .build()
//    }
//}