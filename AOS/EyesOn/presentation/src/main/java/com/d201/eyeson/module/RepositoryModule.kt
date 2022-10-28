package com.d201.eyeson.module

import com.d201.data.datasource.UserRemoteDataSource
import com.d201.data.repository.UserRepositoryImpl
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
    ) : UserRepository{
        return UserRepositoryImpl(userRemoteDataSource)
    }
}