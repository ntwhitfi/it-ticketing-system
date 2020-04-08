package main.kotlin.com.ntwhitfi.it.ticketing.system.request.authorizer.config

import com.nimbusds.jose.proc.SecurityContext
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor
import com.ntwhitfi.it.ticketing.system.auth.util.AuthTokenProcessor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RequestAuthorizerModule {

    @Provides
    @Singleton
    fun getJWTProcessor(): ConfigurableJWTProcessor<SecurityContext> {
        return AuthTokenProcessor().getJWTProcessor()
    }

}