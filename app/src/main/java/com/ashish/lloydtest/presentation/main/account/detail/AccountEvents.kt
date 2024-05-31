package com.ashish.lloydtest.presentation.main.account.detail


sealed class AccountEvents{

    object GetAccount: AccountEvents()

    object Logout: AccountEvents()

    object OnRemoveHeadFromQueue: AccountEvents()
}
