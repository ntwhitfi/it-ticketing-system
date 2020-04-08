package com.ntwhitfi.it.ticketing.system.auth.handler


import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.util.StringUtils
import com.ntwhitfi.it.ticketing.system.auth.config.AuthServiceComponent
import com.ntwhitfi.it.ticketing.system.auth.config.DaggerAuthServiceComponent
import com.ntwhitfi.it.ticketing.system.auth.model.AuthRequest
import com.ntwhitfi.it.ticketing.system.auth.model.AuthResponse
import com.ntwhitfi.it.ticketing.system.auth.service.IAuthService

class AuthHandler: RequestHandler<AuthRequest, AuthResponse> {
    var authService: IAuthService? = null

    constructor(authService: IAuthService): super() {
        this.authService = authService
    }

    constructor(authServiceComponent: AuthServiceComponent): this() {
        AuthHandler(authServiceComponent.authService)
    }

    constructor() {
        AuthHandler(DaggerAuthServiceComponent.builder().build())
    }

    override fun handleRequest(authRequest: AuthRequest, requestContext: Context?): AuthResponse {
        if(StringUtils.isNullOrEmpty(authRequest.userName) || StringUtils.isNullOrEmpty(authRequest.password)) {
            return AuthResponse(400, "Username and password are both required", null)
        }

        var authResponse = authService?.signIn(authRequest.userName, authRequest.password)

        if(authResponse == null) {
            authResponse = AuthResponse(500,
                "Failed to execute auth request, please try again. If problem persists, please contact the system administrator.", null)
        }

        return authResponse
    }
}