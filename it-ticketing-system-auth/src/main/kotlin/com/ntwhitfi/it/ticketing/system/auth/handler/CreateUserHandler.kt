package com.ntwhitfi.it.ticketing.system.auth.handler


import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.util.StringUtils
import com.ntwhitfi.it.ticketing.system.auth.model.AuthRequest
import com.ntwhitfi.it.ticketing.system.auth.model.AuthResponse
import com.ntwhitfi.it.ticketing.system.auth.model.UserRecord
import com.ntwhitfi.it.ticketing.system.auth.service.IAuthService
import com.ntwhitfi.it.ticketing.system.common.model.RepositoryResponse

class CreateUserHandler(val authService: IAuthService): RequestHandler<UserRecord, RepositoryResponse> {
    override fun handleRequest(userRecord: UserRecord, requestContext: Context?): RepositoryResponse {
        if(StringUtils.isNullOrEmpty(userRecord.email) || StringUtils.isNullOrEmpty(userRecord.password)) {
            return RepositoryResponse(400, "Username and password are both required")
        }

        return authService.createUser(userRecord)
    }
}