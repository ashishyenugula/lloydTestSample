package com.ashish.lloydtest.presentation.auth.register

import com.ashish.lloydtest.business.domain.util.Queue
import com.ashish.lloydtest.business.domain.util.StateMessage


data class RegisterState(
    val isLoading: Boolean = false,
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)
