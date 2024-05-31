package com.ashish.lloydtest.presentation.session

import com.ashish.lloydtest.business.domain.models.AuthToken
import com.ashish.lloydtest.business.domain.util.Queue
import com.ashish.lloydtest.business.domain.util.StateMessage


data class SessionState(
    val isLoading: Boolean = false,
    val authToken: AuthToken? = null,
    val didCheckForPreviousAuthUser: Boolean = false,
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)
