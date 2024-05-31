package com.ashish.lloydtest.presentation.auth.login

import com.ashish.lloydtest.business.domain.util.Queue
import com.ashish.lloydtest.business.domain.util.StateMessage


data class LoginState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)
