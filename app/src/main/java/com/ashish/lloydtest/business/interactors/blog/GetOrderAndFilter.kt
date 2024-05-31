package com.ashish.lloydtest.business.interactors.blog


import com.ashish.lloydtest.business.datasource.network.handleUseCaseException
import com.ashish.lloydtest.business.domain.util.DataState
import com.ashish.lloydtest.presentation.main.blog.list.BlogFilterOptions
import com.ashish.lloydtest.presentation.main.blog.list.BlogOrderOptions
import com.ashish.lloydtest.presentation.main.blog.list.OrderAndFilter
import com.ashish.lloydtest.presentation.main.blog.list.getFilterFromValue
import com.ashish.lloydtest.presentation.main.blog.list.getOrderFromValue
import com.ashish.lloydtest.presentation.util.DataStoreKeys
import com.ashish.lloydtest.business.datasource.datastore.AppDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetOrderAndFilter(
    private val appDataStoreManager: AppDataStore
) {
    fun execute(): Flow<DataState<OrderAndFilter>> = flow {
        emit(DataState.loading<OrderAndFilter>())
        val filter = appDataStoreManager.readValue(DataStoreKeys.BLOG_FILTER)?.let { filter ->
            getFilterFromValue(filter)
        }?: getFilterFromValue(BlogFilterOptions.DATE_UPDATED.value)
        val order = appDataStoreManager.readValue(DataStoreKeys.BLOG_ORDER)?.let { order ->
            getOrderFromValue(order)
        }?: getOrderFromValue(BlogOrderOptions.DESC.value)
        emit(DataState.data(
            response = null,
            data = OrderAndFilter(order = order, filter = filter)
        ))
    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}










