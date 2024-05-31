package com.ashish.lloydtest.business.interactors.account

import com.ashish.lloydtest.business.datasource.cache.account.AccountDao
import com.ashish.lloydtest.business.datasource.cache.account.toAccount
import com.ashish.lloydtest.business.datasource.cache.account.toEntity
import com.ashish.lloydtest.business.domain.models.Account
import com.ashish.lloydtest.business.domain.models.AuthToken
import com.ashish.lloydtest.business.domain.util.DataState
import com.ashish.lloydtest.business.domain.util.ErrorHandling.Companion.ERROR_AUTH_TOKEN_INVALID
import com.ashish.lloydtest.business.domain.util.ErrorHandling.Companion.ERROR_UNABLE_TO_RETRIEVE_ACCOUNT_DETAILS
import com.ashish.lloydtest.business.datasource.network.handleUseCaseException
import com.ashish.lloydtest.business.datasource.network.main.OpenApiMainService
import com.ashish.lloydtest.business.datasource.network.main.toAccount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class GetAccount(
    private val service: OpenApiMainService,
    private val cache: AccountDao,
) {
    private val TAG: String = "AppDebug"

    fun execute(
        authToken: AuthToken?,
    ): Flow<DataState<Account>> = flow {
        emit(DataState.loading<Account>())
        if(authToken == null){
            throw Exception(ERROR_AUTH_TOKEN_INVALID)
        }
        // get from network
        val account = service.getAccount("Token ${authToken.token}").toAccount()

        // update/insert into the cache
        cache.insertAndReplace(account.toEntity())

        // emit from cache
        val cachedAccount = cache.searchByPk(account.pk)?.toAccount()

        if(cachedAccount == null){
            throw Exception(ERROR_UNABLE_TO_RETRIEVE_ACCOUNT_DETAILS)
        }

        emit(DataState.data(response = null, cachedAccount))
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}















