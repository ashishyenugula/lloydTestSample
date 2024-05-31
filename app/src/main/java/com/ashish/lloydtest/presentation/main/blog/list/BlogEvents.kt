package com.ashish.lloydtest.presentation.main.blog.list

import com.ashish.lloydtest.business.domain.util.StateMessage


sealed class BlogEvents {

    object NewSearch : BlogEvents()

    object NextPage: BlogEvents()

    data class UpdateQuery(val query: String): BlogEvents()

    data class UpdateFilter(val filter: BlogFilterOptions): BlogEvents()

    data class UpdateOrder(val order: BlogOrderOptions): BlogEvents()

    object GetOrderAndFilter: BlogEvents()

    data class Error(val stateMessage: StateMessage): BlogEvents()

    object OnRemoveHeadFromQueue: BlogEvents()
}
