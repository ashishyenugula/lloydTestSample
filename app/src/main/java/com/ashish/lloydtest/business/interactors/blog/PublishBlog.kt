package com.ashish.lloydtest.business.interactors.blog

import com.ashish.lloydtest.business.datasource.cache.blog.BlogPostDao
import com.ashish.lloydtest.business.datasource.cache.blog.toEntity
import com.ashish.lloydtest.business.datasource.network.handleUseCaseException
import com.ashish.lloydtest.business.datasource.network.main.OpenApiMainService
import com.ashish.lloydtest.business.datasource.network.main.responses.toBlogPost
import com.ashish.lloydtest.business.domain.models.AuthToken
import com.ashish.lloydtest.business.domain.util.DataState
import com.ashish.lloydtest.business.domain.util.ErrorHandling
import com.ashish.lloydtest.business.domain.util.ErrorHandling.Companion.ERROR_AUTH_TOKEN_INVALID
import com.ashish.lloydtest.business.domain.util.MessageType
import com.ashish.lloydtest.business.domain.util.Response
import com.ashish.lloydtest.business.domain.util.SuccessHandling
import com.ashish.lloydtest.business.domain.util.UIComponentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception

class PublishBlog(
    private val service: OpenApiMainService,
    private val cache: BlogPostDao,
){
    private val TAG: String = "AppDebug"

    fun execute(
        authToken: AuthToken?,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?,
    ): Flow<DataState<Response>> = flow {
        emit(DataState.loading<Response>())
        if(authToken == null){
            throw Exception(ERROR_AUTH_TOKEN_INVALID)
        }
        // attempt update
        val createResponse = service.createBlog(
            "Token ${authToken.token}",
            title,
            body,
            image
        )

        // If they don't have a paid membership account it will still return a 200 with failure message
        // Need to account for that
        if (createResponse.response == SuccessHandling.RESPONSE_MUST_BECOME_MEMBER) { // failure
            throw Exception(SuccessHandling.RESPONSE_MUST_BECOME_MEMBER)
        }

        if(createResponse.response == ErrorHandling.GENERIC_ERROR){
            throw Exception(createResponse.errorMessage)
        }

        // insert the new blog into the cache
        cache.insert(createResponse.toBlogPost().toEntity())

        // Tell the UI it was successful
        emit(DataState.data<Response>(
            data = Response(
                message = SuccessHandling.SUCCESS_BLOG_CREATED,
                uiComponentType = UIComponentType.None(),
                messageType = MessageType.Success()
            ),
            response = null,
        ))
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}






















