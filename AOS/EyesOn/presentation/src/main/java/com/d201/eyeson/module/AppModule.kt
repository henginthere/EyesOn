package com.d201.eyeson.module

import android.app.Application
import android.content.Context
import android.util.Base64
import androidx.room.Room
import com.d201.data.db.notifi.EyesOnDatabase
import com.d201.eyeson.util.OPENVIDU_SECRET
import com.d201.eyeson.util.OPENVIDU_URL
import com.d201.webrtc.utils.CustomHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    //SharedPreferences DI
    @Singleton
    @Provides
    fun provideSharedPreferences(app: Application) =
        app.getSharedPreferences("app_preference", Context.MODE_PRIVATE)!!

    @Provides
    fun provideHttpClient() = CustomHttpClient(
        OPENVIDU_URL, "Basic " + Base64.encodeToString(
            "OPENVIDUAPP:$OPENVIDU_SECRET".toByteArray(), Base64.DEFAULT
        ).trim()
    )

    //Room DI
    @Singleton
    @Provides
    fun provideEyesOnDatabase(app: Application) =
        Room.databaseBuilder(app, EyesOnDatabase::class.java, "eyeson_db")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideNotiDao(db: EyesOnDatabase) = db.notiDao()
}