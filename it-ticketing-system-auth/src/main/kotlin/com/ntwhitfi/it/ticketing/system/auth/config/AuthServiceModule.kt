package com.ntwhitfi.it.ticketing.system.auth.config

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder
import com.ntwhitfi.it.ticketing.system.auth.model.UserRecord
import com.ntwhitfi.it.ticketing.system.auth.repository.UserRepository
import com.ntwhitfi.it.ticketing.system.auth.service.AuthService
import com.ntwhitfi.it.ticketing.system.auth.service.IAuthService
import com.ntwhitfi.it.ticketing.system.common.repository.IRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AuthServiceModule {

    @Provides
    @Singleton
    fun getAmazonCognitoIdentityClient(): AWSCognitoIdentityProvider {
        return AWSCognitoIdentityProviderClientBuilder.standard()
            .withRegion(Constants.REGION)
            .build()
    }

    @Provides
    @Singleton
    fun getUserRepository(amazonCognitoIdentityClient: AWSCognitoIdentityProvider): IRepository<UserRecord>{
        return UserRepository(amazonCognitoIdentityClient)
    }

    @Provides
    @Singleton
    fun getAuthService(amazonCognitoIdentityClient: AWSCognitoIdentityProvider, userRepository: IRepository<UserRecord>): IAuthService {
        return AuthService(amazonCognitoIdentityClient, userRepository)
    }
}