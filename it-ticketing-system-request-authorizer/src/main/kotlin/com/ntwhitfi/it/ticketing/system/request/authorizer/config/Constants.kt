package com.ntwhitfi.it.ticketing.system.request.authorizer.config

object Constants {
    val USER_POOL_ID = System.getenv("USER_POOL_ID")
    val REGION = System.getenv("REGION")
    val COGNITO_JWT_URL = "https://cognito-idp.${REGION}.amazonaws.com/${USER_POOL_ID}/.well-known/jwks.json"
}