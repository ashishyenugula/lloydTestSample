package com.ashish.lloydtest.datasource.network.auth

import com.ashish.lloydtest.business.domain.util.ErrorHandling

object RegisterResponses {
    val email = "astest12@gmail.com"
    val password = "123456"
    val pk = 1
    val username = "ashish_test"
    val token = "de803edc9ebefa3dee77faea8f34fff3e6b217b5"

    val registerSuccess = "{ \"response\": \"successfully registered new user.\", \"email\": \"$email\", \"username\": \"$username\", \"pk\": $pk, \"token\": \"$token\" }"
    val registerFail_emailInUse = "{ \"error_message\": \"${ErrorHandling.ERROR_EMAIL_IN_USE}\", \"response\": \"Error\" }"
    val registerFail_usernameInUse = "{ \"error_message\": \"${ErrorHandling.ERROR_USERNAME_IN_USE}\", \"response\": \"Error\" }"
    val registerFail_passwordsMustMatch = "{ \"error_message\": \"${ErrorHandling.ERROR_PASSWORDS_MUST_MATCH}\", \"response\": \"Error\" }"
    val registerFail_usernameMissing = "{ \"error_message\": \"${ErrorHandling.ERROR_USERNAME_BLANK_FIELD}\", \"response\": \"Error\" }"
    val registerFail_emailMissing = "{ \"error_message\": \"${ErrorHandling.ERROR_EMAIL_BLANK_FIELD}\", \"response\": \"Error\" }"
    val registerFail_passwordMissing = "{ \"error_message\": \"${ErrorHandling.ERROR_PASSWORD_BLANK_FIELD}\", \"response\": \"Error\" }"
    val registerFail_password2Missing = "{ \"error_message\": \"${ErrorHandling.ERROR_PASSWORD2_BLANK_FIELD}\", \"response\": \"Error\" }"
}