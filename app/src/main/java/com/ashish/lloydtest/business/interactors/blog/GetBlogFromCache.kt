package com.ashish.lloydtest.business.interactors.blog


import com.ashish.lloydtest.business.datasource.cache.blog.BlogPostDao
import com.ashish.lloydtest.business.datasource.cache.blog.toBlogPost
import com.ashish.lloydtest.business.datasource.network.handleUseCaseException
import com.ashish.lloydtest.business.domain.models.BlogPost
import com.ashish.lloydtest.business.domain.util.DataState
import com.ashish.lloydtest.business.domain.util.ErrorHandling.Companion.ERROR_BLOG_UNABLE_TO_RETRIEVE
import com.ashish.lloydtest.business.domain.util.MessageType
import com.ashish.lloydtest.business.domain.util.Response
import com.ashish.lloydtest.business.domain.util.UIComponentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetBlogFromCache(
    private val cache: BlogPostDao,
) {

    fun execute(
        pk: Int,
    ): Flow<DataState<BlogPost>> = flow{
        emit(DataState.loading<BlogPost>())
        val blogPost = cache.getBlogPost(pk)?.toBlogPost()

        if(blogPost != null){
            emit(DataState.data(response = null, data = blogPost))
        }
        else{
            emit(DataState.error<BlogPost>(
                response = Response(
                    message = ERROR_BLOG_UNABLE_TO_RETRIEVE,
                    uiComponentType = UIComponentType.Dialog(),
                    messageType = MessageType.Error()
                )
            ))
        }
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}



















