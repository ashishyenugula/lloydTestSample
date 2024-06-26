package com.ashish.lloydtest.business.interactors.blog


import com.ashish.lloydtest.business.datasource.network.handleUseCaseException
import com.ashish.lloydtest.business.datasource.network.main.OpenApiMainService
import com.ashish.lloydtest.business.domain.models.AuthToken
import com.ashish.lloydtest.business.domain.util.DataState
import com.ashish.lloydtest.business.domain.util.ErrorHandling.Companion.ERROR_AUTH_TOKEN_INVALID
import com.ashish.lloydtest.business.domain.util.ErrorHandling.Companion.GENERIC_ERROR
import com.ashish.lloydtest.business.domain.util.MessageType
import com.ashish.lloydtest.business.domain.util.Response
import com.ashish.lloydtest.business.domain.util.SuccessHandling.Companion.RESPONSE_HAS_PERMISSION_TO_EDIT
import com.ashish.lloydtest.business.domain.util.SuccessHandling.Companion.RESPONSE_NO_PERMISSION_TO_EDIT
import com.ashish.lloydtest.business.domain.util.UIComponentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class IsAuthorOfBlogPost(
    private val service: OpenApiMainService,
) {

    fun execute(
        authToken: AuthToken?,
        slug: String,
    ): Flow<DataState<Boolean>> = flow {
        emit(DataState.loading<Boolean>())
        if(authToken == null){
            throw Exception(ERROR_AUTH_TOKEN_INVALID)
        }
        try{ // catch network exception
            val response = service.isAuthorOfBlogPost(
                "Token ${authToken.token}",
                slug
            )
            if(response.response == RESPONSE_HAS_PERMISSION_TO_EDIT){
                emit(DataState.data(response = null, true))
            }
            else if(response.response == GENERIC_ERROR
                && response.errorMessage == RESPONSE_NO_PERMISSION_TO_EDIT){
                emit(DataState.data(
                    response = Response(
                        message = response.errorMessage,
                        uiComponentType = UIComponentType.None(),
                        messageType = MessageType.Success()
                    ),
                    data = false
                ))
            }
            else if(response.response == GENERIC_ERROR){
                emit(DataState.data(
                    response = Response(
                        message = response.errorMessage,
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    ),
                    data = false
                ))
            }
            else{
                emit(DataState.data(response = null, false))
            }
        }catch (e: Exception){
            emit(DataState.data(response = null, false))
        }
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}



























