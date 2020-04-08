package com.ntwhitfi.it.ticketing.system.auth.service

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest
import com.amazonaws.util.StringUtils
import com.ntwhitfi.it.ticketing.system.auth.config.Constants
import com.ntwhitfi.it.ticketing.system.auth.model.AuthResponse
import com.ntwhitfi.it.ticketing.system.auth.model.UserRecord
import com.ntwhitfi.it.ticketing.system.common.model.RepositoryResponse
import com.ntwhitfi.it.ticketing.system.common.repository.IRepository
import mu.KotlinLogging
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

class AuthService(val cognitoClient: AWSCognitoIdentityProvider, val userRepository: IRepository<UserRecord>) : IAuthService {
    private val logger = KotlinLogging.logger {}
    override fun signIn(userName: String, password: String): AuthResponse {
        val authParams = HashMap<String, String>()
        authParams["USERNAME"] = userName
        authParams["PASSWORD"] = password

        val authRequest = AdminInitiateAuthRequest()
            .withClientId(Constants.COGNITO_CLIENT_ID)
            .withUserPoolId(Constants.USER_POOL_ID)
            .withAuthParameters(authParams)

        try {
            val result = cognitoClient.adminInitiateAuth(authRequest)

            //check if any challenges were set in the auth result
            return if(StringUtils.isNullOrEmpty(result.challengeName)) {
                val authResultType = result.authenticationResult

                //return the auth token if it was received
                if(!StringUtils.isNullOrEmpty(authResultType.accessToken)) {
                    AuthResponse(200, "$userName successfully authenticated", authResultType.accessToken)
                } else {
                    logger.error("Unable to authenticate user ${userName}.")
                    AuthResponse(401, "Failed to authenticate user $userName", null)
                }
            } else {
                logger.error("Unable to authenticate user ${userName}. Auth challenge received: ${result.challengeName}")
                AuthResponse(401, "Failed to authenticate user $userName", null)
            }
        } catch (ex: Exception) {
            logger.error("Unable to authenticate user ${userName}. Exception: $ex")
            return AuthResponse(400, "Failed to authenticate user $userName", null)
        }
    }

    override fun createUser(userRecord: UserRecord): RepositoryResponse {
        userRecord.id = UUID.randomUUID().toString()
        return userRepository.add(userRecord)
    }
}