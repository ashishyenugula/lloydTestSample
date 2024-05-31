package com.ashish.lloydtest.business.interactors.blog


import com.ashish.lloydtest.business.datasource.cache.blog.BlogPostDao
import com.ashish.lloydtest.business.datasource.cache.blog.returnOrderedBlogQuery
import com.ashish.lloydtest.business.datasource.cache.blog.toBlogPost
import com.ashish.lloydtest.business.datasource.cache.blog.toEntity
import com.ashish.lloydtest.business.datasource.network.handleUseCaseException
import com.ashish.lloydtest.business.datasource.network.main.OpenApiMainService
import com.ashish.lloydtest.business.datasource.network.main.toBlogPost
import com.ashish.lloydtest.business.domain.models.AuthToken
import com.ashish.lloydtest.business.domain.models.BlogPost
import com.ashish.lloydtest.business.domain.util.DataState
import com.ashish.lloydtest.business.domain.util.ErrorHandling.Companion.ERROR_AUTH_TOKEN_INVALID
import com.ashish.lloydtest.business.domain.util.MessageType
import com.ashish.lloydtest.business.domain.util.Response
import com.ashish.lloydtest.business.domain.util.UIComponentType
import com.ashish.lloydtest.presentation.main.blog.list.BlogFilterOptions
import com.ashish.lloydtest.presentation.main.blog.list.BlogOrderOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class SearchBlogs(
    private val service: OpenApiMainService,
    private val cache: BlogPostDao,
) {

    private val TAG: String = "AppDebug"

    fun execute(
        authToken: AuthToken?,
        query: String,
        page: Int,
        filter: BlogFilterOptions,
        order: BlogOrderOptions,
    ): Flow<DataState<List<BlogPost>>> = flow {
        emit(DataState.loading<List<BlogPost>>())
        if(authToken == null){
            throw Exception(ERROR_AUTH_TOKEN_INVALID)
        }
        // get Blogs from network
        val filterAndOrder = order.value + filter.value // Ex: -date_updated

        try{ // catch network exception
            val blogs = service.searchListBlogPosts(
                "Token ${authToken.token}",
                query = query,
                ordering = filterAndOrder,
                page = page
            ).results.map { it.toBlogPost() }

            // Insert into cache
            for(blog in blogs){
                try{
                    cache.insert(blog.toEntity())
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }catch (e: Exception){
            emit(
                DataState.error<List<BlogPost>>(
                    response = Response(
                        message = "Unable to update the cache.",
                        uiComponentType = UIComponentType.None(),
                        messageType = MessageType.Error()
                    )
                )
            )
        }

        // emit from cache
        val cachedBlogs = cache.returnOrderedBlogQuery(
            query = query,
            filterAndOrder = filterAndOrder,
            page = page
        ).map { it.toBlogPost() }

        emit(DataState.data(response = null, data = cachedBlogs))
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}



















