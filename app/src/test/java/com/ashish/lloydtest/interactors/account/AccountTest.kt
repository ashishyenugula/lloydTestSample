package com.ashish.lloydtest.interactors.account

import com.ashish.lloydtest.business.datasource.network.main.OpenApiMainService
import com.ashish.lloydtest.business.domain.models.AuthToken
import com.ashish.lloydtest.business.interactors.account.GetAccount
import com.ashish.lloydtest.datasource.cache.AccountDaoTest
import com.ashish.lloydtest.datasource.cache.AppDatabaseTest
import com.ashish.lloydtest.datasource.network.account.AccountResponses
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

/**
 * 1. Retrieve Account details from network and cache
 */
class AccountTest {

    private val appDatabase = AppDatabaseTest()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl

    // system in test
    private lateinit var getAccount: GetAccount

    // dependencies
    private lateinit var service: OpenApiMainService
    private lateinit var cache: AccountDaoTest

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        baseUrl = mockWebServer.url("/api/recipe/")
        service = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(OpenApiMainService::class.java)

        cache = AccountDaoTest(appDatabase)

        // instantiate the system in test
        getAccount = GetAccount(
            service = service,
            cache = cache,
        )
    }

    @Test
    fun getAccountSuccess() = runBlocking {
        // condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(AccountResponses.getAccountSuccess)
        )

        // User Information
        val pk = AccountResponses.pk
        val email = AccountResponses.email
        val username = AccountResponses.username
        val password = AccountResponses.password
        val token = AccountResponses.token

        // confirm no Account is stored in cache
        var cachedAccount = cache.searchByEmail(email)
        assert(cachedAccount == null)

        val emissions = getAccount.execute(
            authToken = AuthToken(
                accountPk = pk,
                token = token,
            )
        ).toList()

        // first emission should be `loading`
        assert(emissions[0].isLoading)

        // confirm Account is cached
        cachedAccount = cache.searchByPk(pk)
        assert(cachedAccount?.email == email)
        assert(cachedAccount?.username == username)
        assert(cachedAccount?.pk == pk)

        // confirm second emission is the cached Account
        assert(emissions[1].data?.pk == pk)
        assert(emissions[1].data?.email == email)
        assert(emissions[1].data?.username == username)

        // loading done
        assert(!emissions[1].isLoading)
    }
}




















