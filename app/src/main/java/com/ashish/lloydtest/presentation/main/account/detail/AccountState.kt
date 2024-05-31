package com.ashish.lloydtest.presentation.main.account.detail

import com.ashish.lloydtest.business.domain.models.Account
import com.ashish.lloydtest.business.domain.util.Queue
import com.ashish.lloydtest.business.domain.util.StateMessage


data class AccountState(
    val isLoading: Boolean = false,
    val account: Account? = null,
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)
