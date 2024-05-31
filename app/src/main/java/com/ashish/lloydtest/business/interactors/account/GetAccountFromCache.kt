package com.ashish.lloydtest.business.interactors.account


import com.ashish.lloydtest.business.datasource.cache.account.AccountDao
import com.ashish.lloydtest.business.datasource.cache.account.toAccount
import com.ashish.lloydtest.business.datasource.network.handleUseCaseException
import com.ashish.lloydtest.business.domain.models.Account
import com.ashish.lloydtest.business.domain.util.DataState
import com.ashish.lloydtest.business.domain.util.ErrorHandling
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class GetAccountFromCache(
    private val cache: AccountDao,
) {
    fun execute(
        pk: Int,
    ): Flow<DataState<Account>> = flow {
        emit(DataState.loading<Account>())
        // emit from cache
        val cachedAccount = cache.searchByPk(pk)?.toAccount()

        if(cachedAccount == null){
            throw Exception(ErrorHandling.ERROR_UNABLE_TO_RETRIEVE_ACCOUNT_DETAILS)
        }

        emit(DataState.data(response = null, cachedAccount))
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}















