package com.ntwhitfi.it.ticketing.system.auth.config

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider
import com.ntwhitfi.it.ticketing.system.auth.model.UserRecord
import com.ntwhitfi.it.ticketing.system.auth.service.IAuthService
import com.ntwhitfi.it.ticketing.system.common.repository.IRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AuthServiceModule::class] )
interface AuthServiceComponent {
    val authService: IAuthService
    val cognitoIdentityClient: AWSCognitoIdentityProvider
    val userRepository: IRepository<UserRecord>
}