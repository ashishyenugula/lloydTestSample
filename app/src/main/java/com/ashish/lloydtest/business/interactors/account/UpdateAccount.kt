package com.ashish.lloydtest.business.interactors.account


import com.ashish.lloydtest.business.datasource.cache.account.AccountDao
import com.ashish.lloydtest.business.datasource.network.handleUseCaseException
import com.ashish.lloydtest.business.datasource.network.main.OpenApiMainService
import com.ashish.lloydtest.business.domain.models.AuthToken
import com.ashish.lloydtest.business.domain.util.DataState
import com.ashish.lloydtest.business.domain.util.ErrorHandling
import com.ashish.lloydtest.business.domain.util.ErrorHandling.Companion.GENERIC_ERROR
import com.ashish.lloydtest.business.domain.util.MessageType
import com.ashish.lloydtest.business.domain.util.Response
import com.ashish.lloydtest.business.domain.util.SuccessHandling.Companion.SUCCESS_ACCOUNT_UPDATED
import com.ashish.lloydtest.business.domain.util.UIComponentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class UpdateAccount(
    private val service: OpenApiMainService,
    private val cache: AccountDao,
) {
    fun execute(
        authToken: AuthToken?,
        pk: Int?,
        email: String,
        username: String,
    ): Flow<DataState<Response>> = flow {
        emit(DataState.loading<Response>())
        if(authToken == null){
            throw Exception(ErrorHandling.ERROR_AUTH_TOKEN_INVALID)
        }
        if(pk == null){
            throw Exception(ErrorHandling.ERROR_PK_INVALID)
        }

        // Update network
        val response = service.updateAccount(
            authorization = "Token ${authToken.token}",
            email = email,
            username = username
        )

        if(response.response == GENERIC_ERROR){
            throw Exception(response.errorMessage)
        }
        else if(response.response != SUCCESS_ACCOUNT_UPDATED){
            throw Exception(ErrorHandling.ERROR_UPDATE_ACCOUNT)
        }

        // update cache
        cache.updateAccount(
            pk = pk,
            email = email,
            username = username
        )

        // Tell the UI it was successful
        emit(DataState.data<Response>(
            data = Response(
                message = SUCCESS_ACCOUNT_UPDATED,
                uiComponentType = UIComponentType.Toast(),
                messageType = MessageType.Success()
            ),
            response = null
        ))
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}
