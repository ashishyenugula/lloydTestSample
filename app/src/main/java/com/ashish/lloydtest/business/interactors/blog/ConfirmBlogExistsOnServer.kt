package com.ashish.lloydtest.business.interactors.blog

import com.ashish.lloydtest.business.datasource.cache.blog.BlogPostDao
import com.ashish.lloydtest.business.datasource.network.handleUseCaseException
import com.ashish.lloydtest.business.datasource.network.main.OpenApiMainService
import com.ashish.lloydtest.business.domain.models.AuthToken
import com.ashish.lloydtest.business.domain.util.DataState
import com.ashish.lloydtest.business.domain.util.ErrorHandling
import com.ashish.lloydtest.business.domain.util.ErrorHandling.Companion.UNABLE_TO_RESOLVE_HOST
import com.ashish.lloydtest.business.domain.util.MessageType
import com.ashish.lloydtest.business.domain.util.Response
import com.ashish.lloydtest.business.domain.util.SuccessHandling
import com.ashish.lloydtest.business.domain.util.SuccessHandling.Companion.SUCCESS_BLOG_DOES_NOT_EXIST_IN_CACHE
import com.ashish.lloydtest.business.domain.util.UIComponentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.lang.Exception

/**
 * If a blog exist in the cache but does not exist on server, we need to delete from cache.
 */
class ConfirmBlogExistsOnServer(
    private val service: OpenApiMainService,
    private val cache: BlogPostDao
) {

    fun execute(
        authToken: AuthToken?,
        pk: Int,
        slug: String,
    ): Flow<DataState<Response>> =  flow {
        emit(DataState.loading<Response>())
        val cachedBlog = cache.getBlogPost(pk)
        if(cachedBlog == null){
            // It doesn't exist in cache. Finish.
            emit(DataState.data<Response>(
                data = Response(
                    message = SUCCESS_BLOG_DOES_NOT_EXIST_IN_CACHE,
                    uiComponentType = UIComponentType.None(),
                    messageType = MessageType.Success()
                ),
                response = null,
            ))
        }else{
            if(authToken == null){
                throw Exception(ErrorHandling.ERROR_AUTH_TOKEN_INVALID)
            }
            // confirm it exists on server (throws 404 if does not exist)
            var isNetworkError = false
            val blogPost = try {
                service.getBlog(
                    authorization = "Token ${authToken.token}",
                    slug = slug,
                )
            }catch (e1: Exception){
                if(e1.message?.contains(UNABLE_TO_RESOLVE_HOST) == true){ // network error
                    isNetworkError = true
                }
                e1.printStackTrace()
                null
            }
            if(isNetworkError){
                emit(
                    DataState.error<Response>(
                        response = Response(
                            message = "Network Error.",
                            uiComponentType = UIComponentType.None(),
                            messageType = MessageType.Error()
                        )
                    )
                )
            }
            else{
                // if it exists on server but not in cache. Delete from cache and emit error.
                if(blogPost == null){
                    cache.deleteBlogPost(pk)
                    emit(DataState.error<Response>(
                        response = Response(
                            message = ErrorHandling.ERROR_BLOG_DOES_NOT_EXIST,
                            uiComponentType = UIComponentType.Dialog(),
                            messageType = MessageType.Error()
                        )
                    ))
                }else{ // if it exists in the cache and on the server. Everything is fine.
                    emit(DataState.data<Response>(
                        data = Response(
                            message = SuccessHandling.SUCCESS_BLOG_EXISTS_ON_SERVER,
                            uiComponentType = UIComponentType.None(),
                            messageType = MessageType.Success()
                        ),
                        response = null,
                    ))
                }
            }
        }
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}
















