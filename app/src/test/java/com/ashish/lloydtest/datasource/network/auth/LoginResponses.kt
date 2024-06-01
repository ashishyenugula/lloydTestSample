package com.ashish.lloydtest.datasource.network.auth

import com.ashish.lloydtest.business.domain.util.ErrorHandling

object LoginResponses {
    val email = "astest12@gmail.com"
    val password = "123456"
    val pk = 1
    val username = "ashish_test"
    val token = "de803edc9ebefa3dee77faea8f34fff3e6b217b5"

    val loginSuccess = "{ \"response\": \"Successfully authenticated.\", \"pk\": $pk, \"email\": \"$email\", \"token\": \"$token\" }"
    val loginFail = "{ \"response\": \"Error\", \"error_message\": \"${ErrorHandling.INVALID_CREDENTIALS}\" }"


}