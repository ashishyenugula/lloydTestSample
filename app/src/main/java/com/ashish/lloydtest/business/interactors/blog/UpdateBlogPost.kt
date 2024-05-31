package com.ashish.lloydtest.business.interactors.blog



import com.ashish.lloydtest.business.datasource.cache.blog.BlogPostDao
import com.ashish.lloydtest.business.datasource.network.handleUseCaseException
import com.ashish.lloydtest.business.datasource.network.main.OpenApiMainService
import com.ashish.lloydtest.business.domain.models.AuthToken
import com.ashish.lloydtest.business.domain.util.DataState
import com.ashish.lloydtest.business.domain.util.ErrorHandling.Companion.ERROR_AUTH_TOKEN_INVALID
import com.ashish.lloydtest.business.domain.util.ErrorHandling.Companion.GENERIC_ERROR
import com.ashish.lloydtest.business.domain.util.MessageType
import com.ashish.lloydtest.business.domain.util.Response
import com.ashish.lloydtest.business.domain.util.SuccessHandling.Companion.SUCCESS_BLOG_UPDATED
import com.ashish.lloydtest.business.domain.util.UIComponentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UpdateBlogPost(
    private val service: OpenApiMainService,
    private val cache: BlogPostDao,
) {

    /**
     * If successful this will emit a string saying: 'updated'
     */
    fun execute(
        authToken: AuthToken?,
        slug: String,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?,
    ): Flow<DataState<Response>> = flow{
        emit(DataState.loading<Response>())
        if(authToken == null){
            throw Exception(ERROR_AUTH_TOKEN_INVALID)
        }
        // attempt update
        val createUpdateResponse = service.updateBlog(
            "Token ${authToken.token}",
            slug,
            title,
            body,
            image
        )

        if(createUpdateResponse.response == GENERIC_ERROR){ // failure
            throw Exception(createUpdateResponse.errorMessage)
        }else{ // success
            cache.updateBlogPost(
                pk = createUpdateResponse.pk,
                title = createUpdateResponse.title,
                body = createUpdateResponse.body,
                image = createUpdateResponse.image
            )
            // Tell the UI it was successful
            emit(DataState.data<Response>(
                data = Response(
                    message = SUCCESS_BLOG_UPDATED,
                    uiComponentType = UIComponentType.None(),
                    messageType = MessageType.Success()
                ),
                response = null,
            ))
        }
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}









