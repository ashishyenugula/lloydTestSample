package com.ashish.lloydtest.presentation.main.blog.list

import com.ashish.lloydtest.presentation.main.blog.list.BlogFilterOptions
import com.ashish.lloydtest.presentation.main.blog.list.BlogOrderOptions

data class OrderAndFilter(
    val order: BlogOrderOptions,
    val filter: BlogFilterOptions,
)
