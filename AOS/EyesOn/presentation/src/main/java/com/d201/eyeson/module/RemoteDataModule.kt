package com.d201.eyeson.module


import com.d201.data.api.ComplaintsApi
import com.d201.data.api.HelpApi
import com.d201.data.api.UserApi
import com.d201.eyeson.util.BASE_URL
import com.d201.eyeson.util.XAccessTokenInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataModule {


    // HttpLoggingInterceptor DI
    @Provides
    @Singleton
    fun provideInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    //OkHttpClient DI
    @Provides
    @Singleton
    fun provideOkHttpClient(
        xAccessTokenInterceptor: XAccessTokenInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(xAccessTokenInterceptor)
            .build()
    }

    // Gson DI
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().setLenient().create()
    }

    //Retrofit DI
    @Provides
    @Singleton
    @Named("mainRetrofit")
    fun provideRetrofitInstance(gson: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideUserApi(@Named("mainRetrofit") retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideComplaintsApi(@Named("mainRetrofit") retrofit: Retrofit): ComplaintsApi {
        return retrofit.create(ComplaintsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideHelpApi(@Named("mainRetrofit") retrofit: Retrofit): HelpApi {
        return retrofit.create(HelpApi::class.java)
    }
}