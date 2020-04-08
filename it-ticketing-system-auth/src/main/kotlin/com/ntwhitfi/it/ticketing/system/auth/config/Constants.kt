package com.ntwhitfi.it.ticketing.system.auth.config

object Constants {
    val COGNITO_CLIENT_ID = System.getenv("COGNITO_CLIENT_ID")
    val USER_POOL_ID = System.getenv("USER_POOL_ID")
    val REGION = System.getenv("REGION")
}