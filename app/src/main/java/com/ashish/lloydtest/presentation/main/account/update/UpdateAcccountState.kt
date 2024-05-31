package com.ashish.lloydtest.presentation.main.account.update

import com.ashish.lloydtest.business.domain.models.Account
import com.ashish.lloydtest.business.domain.util.Queue
import com.ashish.lloydtest.business.domain.util.StateMessage


data class UpdateAccountState(
    val isLoading: Boolean = false,
    val isUpdateComplete: Boolean = false,
    val account: Account? = null,
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)
