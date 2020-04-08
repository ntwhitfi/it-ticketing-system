package com.ntwhitfi.it.ticketing.system.auth.util

import com.nimbusds.jose.JWSAlgorithm.RS256
import com.nimbusds.jose.jwk.source.RemoteJWKSet
import com.nimbusds.jose.proc.JWSVerificationKeySelector
import com.nimbusds.jose.proc.SecurityContext
import com.nimbusds.jose.util.DefaultResourceRetriever
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor
import com.nimbusds.jwt.proc.DefaultJWTProcessor
import com.ntwhitfi.it.ticketing.system.request.authorizer.config.Constants
import java.net.URL

class AuthTokenProcessor {

     fun getJWTProcessor(): ConfigurableJWTProcessor<SecurityContext> {
        val resourceRetriever = DefaultResourceRetriever(2000,2000)
        val jwkSetURL= URL(Constants.COGNITO_JWT_URL);

        //Creates the JSON Web Key (JWK)
        val keySource= RemoteJWKSet<SecurityContext>(jwkSetURL, resourceRetriever);
        var jwtProcessor = DefaultJWTProcessor<SecurityContext>()

        //RSASSA-PKCS-v1_5 using SHA-256 hash algorithm
        val keySelector= JWSVerificationKeySelector(RS256, keySource);
        jwtProcessor.setJWSKeySelector(keySelector);
        return jwtProcessor;
    }
}