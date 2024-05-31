package com.ashish.lloydtest.di.auth

import com.ashish.lloydtest.business.datasource.cache.account.AccountDao
import com.ashish.lloydtest.business.datasource.cache.auth.AuthTokenDao
import com.ashish.lloydtest.business.datasource.network.auth.OpenApiAuthService
import com.ashish.lloydtest.business.interactors.auth.Login
import com.ashish.lloydtest.business.interactors.auth.Register
import com.ashish.lloydtest.business.interactors.session.CheckPreviousAuthUser
import com.ashish.lloydtest.business.interactors.session.Logout

import com.ashish.lloydtest.business.datasource.datastore.AppDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.FlowPreview
import retrofit2.Retrofit
import javax.inject.Singleton

@FlowPreview
@Module
@InstallIn(SingletonComponent::class)
object AuthModule{

    @Singleton
    @Provides
    fun provideOpenApiAuthService(retrofitBuilder: Retrofit.Builder): OpenApiAuthService {
        return retrofitBuilder
            .build()
            .create(OpenApiAuthService::class.java)
    }

    @Singleton
    @Provides
    fun provideCheckPrevAuthUser(
        accountDao: AccountDao,
        authTokenDao: AuthTokenDao,
    ): CheckPreviousAuthUser {
        return CheckPreviousAuthUser(
            accountDao,
            authTokenDao
        )
    }

    @Singleton
    @Provides
    fun provideLogin(
        service: OpenApiAuthService,
        accountDao: AccountDao,
        authTokenDao: AuthTokenDao,
        appDataStoreManager: AppDataStore,
    ): Login {
        return Login(
            service,
            accountDao,
            authTokenDao,
            appDataStoreManager
        )
    }

    @Singleton
    @Provides
    fun provideLogout(
        authTokenDao: AuthTokenDao,
    ): Logout {
        return Logout(authTokenDao)
    }

    @Singleton
    @Provides
    fun provideRegister(
        service: OpenApiAuthService,
        accountDao: AccountDao,
        authTokenDao: AuthTokenDao,
        appDataStoreManager: AppDataStore,
    ): Register {
        return Register(
            service,
            accountDao,
            authTokenDao,
            appDataStoreManager
        )
    }
}









