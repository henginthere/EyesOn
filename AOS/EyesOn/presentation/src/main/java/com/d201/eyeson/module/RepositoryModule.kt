package com.d201.eyeson.module

import com.d201.data.datasource.ComplaintsRemoteDataSource
import com.d201.data.datasource.HelpRemoteDataSource
import com.d201.data.datasource.NotiLocalDataSource
import com.d201.data.datasource.UserRemoteDataSource
import com.d201.data.repository.ComplaintsRepositoryImpl
import com.d201.data.repository.HelpRepositoryImpl
import com.d201.data.repository.NotiRepositoryImpl
import com.d201.data.repository.UserRepositoryImpl
import com.d201.domain.repository.ComplaintsRepository
import com.d201.domain.repository.HelpRepository
import com.d201.domain.repository.NotiRepository
import com.d201.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        userRemoteDataSource: UserRemoteDataSource
    ): UserRepository {
        return UserRepositoryImpl(userRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideComplaintsRepository(
        complaintsRemoteDataSource: ComplaintsRemoteDataSource
    ): ComplaintsRepository {
        return ComplaintsRepositoryImpl(complaintsRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideHelpRepository(
        helpRemoteDataSource: HelpRemoteDataSource
    ): HelpRepository {
        return HelpRepositoryImpl(helpRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideNotiRepository(
        notiLocalDataSource: NotiLocalDataSource
    ): NotiRepository {
        return NotiRepositoryImpl(notiLocalDataSource)
    }
}