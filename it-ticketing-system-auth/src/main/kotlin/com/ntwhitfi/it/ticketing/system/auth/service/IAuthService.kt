package com.ntwhitfi.it.ticketing.system.auth.service

import com.ntwhitfi.it.ticketing.system.auth.model.AuthResponse
import com.ntwhitfi.it.ticketing.system.auth.model.UserRecord
import com.ntwhitfi.it.ticketing.system.common.model.RepositoryResponse

interface IAuthService {
    fun signIn(userName: String, password: String) : AuthResponse
    fun createUser(userRecord: UserRecord): RepositoryResponse
}