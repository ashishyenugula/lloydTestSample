package com.ashish.lloydtest.presentation.main.blog.detail

import com.ashish.lloydtest.business.domain.models.BlogPost
import com.ashish.lloydtest.business.domain.util.Queue
import com.ashish.lloydtest.business.domain.util.StateMessage


data class ViewBlogState(
    val isLoading: Boolean = false,
    val isDeleteComplete: Boolean = false,
    val blogPost: BlogPost? = null,
    val isAuthor: Boolean = false,
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)
