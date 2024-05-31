package com.ashish.lloydtest.presentation.auth.forgot_password

import com.ashish.lloydtest.business.domain.util.Queue
import com.ashish.lloydtest.business.domain.util.StateMessage


data class ForgotPasswordState(
    val isLoading: Boolean = false,
    val isPasswordResetLinkSent: Boolean = false,
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)
