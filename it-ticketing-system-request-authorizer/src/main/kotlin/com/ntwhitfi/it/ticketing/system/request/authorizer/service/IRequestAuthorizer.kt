package main.kotlin.com.ntwhitfi.it.ticketing.system.request.authorizer.service

import main.kotlin.com.ntwhitfi.it.ticketing.system.request.authorizer.model.RequestAuthorizerResponse

interface IRequestAuthorizer {

    fun validateAuthToken(authToken: String): RequestAuthorizerResponse
    fun getJWTPrincipalId(jwt: String): String
}