package com.ntwhitfi.it.ticketing.system.request.authorizer.service

import com.nimbusds.jose.proc.SecurityContext
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor
import main.kotlin.com.ntwhitfi.it.ticketing.system.request.authorizer.model.RequestAuthorizerResponse
import main.kotlin.com.ntwhitfi.it.ticketing.system.request.authorizer.service.IRequestAuthorizer
import java.util.*
import com.google.gson.Gson
import com.ntwhitfi.it.ticketing.system.request.authorizer.config.Constants

class RequestAuthorizer(val jwtProcessor: ConfigurableJWTProcessor<SecurityContext>): IRequestAuthorizer {
    val JWT_SPLIT_CHARS = "\\."
    val JWT_PAYLOAD_INDEX = 1
    val JWT_PARTS_COUNT = 3

    override fun validateAuthToken(authToken: String): RequestAuthorizerResponse {
        if(isValidJWT(authToken)) {
            return RequestAuthorizerResponse(200, "Auth token validated successfully")
        } else {
            return RequestAuthorizerResponse(403, "Invalid auth token provided ")
        }
    }

    private fun isValidJWT(jwt: String): Boolean {
        val jwtParts = jwt.split(JWT_SPLIT_CHARS)

        //validate JWT part size and if the token claim is correct
        if(jwtParts.size != JWT_PARTS_COUNT || getJWTClaim(jwt, Constants.COGNITO_JWT_URL) == null) {
            return false
        }

        return true
    }

    private fun getJWTPayload(jwt: String): String {
        //split jwt and get the payload index
        val payload = jwt.split("\\.")[JWT_PAYLOAD_INDEX]

        //decode the payload
        val decoder = Base64.getDecoder()
        val decodedPayload = decoder.decode(payload)

        //format the payload as UTF_8 and convert to json string
        val formattedPayload = decodedPayload.toString(Charsets.UTF_8)
        return Gson().toJson(formattedPayload)
    }

    private fun getJWTClaim(jwt: String, claim: String): String? {
        //get jwtPayload json and try to retrieve provided claim
        val jwtPayload = Gson().fromJson(getJWTPayload(jwt), HashMap<String, Object>().javaClass)
        val claimValue = jwtPayload[claim]

        //if the claim is found, return it. Otherwise, return null
        if(claimValue != null) {
            return claimValue.toString()
        }

        return null
    }

    override fun getJWTPrincipalId(jwt: String): String {
        val jwtPayload = Gson().fromJson(getJWTPayload(jwt), HashMap<String, Object>().javaClass)
        return jwtPayload["cognito:username"].toString()
    }
}