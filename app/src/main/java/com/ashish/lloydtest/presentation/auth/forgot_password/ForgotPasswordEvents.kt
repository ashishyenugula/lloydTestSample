package com.ashish.lloydtest.presentation.auth.forgot_password

import com.ashish.lloydtest.business.domain.util.StateMessage

sealed class ForgotPasswordEvents {

    object OnPasswordResetLinkSent: ForgotPasswordEvents()

    data class Error(val stateMessage: StateMessage): ForgotPasswordEvents()

    object OnRemoveHeadFromQueue: ForgotPasswordEvents()
}
