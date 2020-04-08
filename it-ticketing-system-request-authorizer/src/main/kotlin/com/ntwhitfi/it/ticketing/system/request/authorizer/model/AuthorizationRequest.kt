package com.ntwhitfi.it.ticketing.system.request.authorizer.model

class AuthorizationRequest(val type: String, val authorizationToken: String, val methodArn: String) {
}