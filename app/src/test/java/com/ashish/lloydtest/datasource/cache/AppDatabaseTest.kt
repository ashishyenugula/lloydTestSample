package com.ashish.lloydtest.datasource.cache

import com.ashish.lloydtest.business.datasource.cache.account.AccountEntity
import com.ashish.lloydtest.business.datasource.cache.auth.AuthTokenEntity
import com.ashish.lloydtest.business.datasource.cache.blog.BlogPostEntity


class AppDatabaseTest {

    // test db tables
    val blogs = mutableListOf<BlogPostEntity>()
    val accounts = mutableListOf<AccountEntity>()
    val authTokens = mutableListOf<AuthTokenEntity>()

}