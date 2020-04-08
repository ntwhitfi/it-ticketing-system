package com.ntwhitfi.it.ticketing.system.auth.repository

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider
import com.amazonaws.services.cognitoidp.model.*
import com.amazonaws.util.StringUtils
import com.ntwhitfi.it.ticketing.system.auth.config.Constants
import com.ntwhitfi.it.ticketing.system.auth.model.UserRecord
import com.ntwhitfi.it.ticketing.system.common.model.RepositoryResponse
import com.ntwhitfi.it.ticketing.system.common.repository.IRepository
import org.passay.CharacterRule
import org.passay.EnglishCharacterData
import org.passay.PasswordGenerator

class UserRepository<T : UserRecord>(val cognitoClient: AWSCognitoIdentityProvider) : IRepository<T> {

    override fun add(item: T): RepositoryResponse {
        val tempPass = createTempPass()
        val createUserRequest = getCreateUserRequest(item, tempPass)
        cognitoClient.adminCreateUser(createUserRequest)

        return updateUserPass(item.email, item.password, tempPass)
    }

    override fun update(item: T): RepositoryResponse {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getById(id: String): RepositoryResponse {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun remove(id: String): RepositoryResponse {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getCreateUserRequest(item: T, tempPass: String) : AdminCreateUserRequest {
        return AdminCreateUserRequest()
            .withUserPoolId(Constants.USER_POOL_ID)
            .withUsername(item.email)
            .withUserAttributes(
                AttributeType()
                    .withValue(item.email),
                AttributeType()
                    .withName("name")
                    .withValue(item.firstName),
                AttributeType()
                    .withName("family_name")
                    .withValue(item.lastName),
                AttributeType()
                    .withName("email_verified")
                    .withValue("true")
            )
            .withTemporaryPassword(tempPass)
            .withMessageAction("SUPPRESS")
            .withDesiredDeliveryMediums(DeliveryMediumType.EMAIL)
            .withForceAliasCreation(false)
    }

    private fun updateUserPass(userName: String, password: String, tempPass: String) : RepositoryResponse{
        val authParams = HashMap<String, String>()
        authParams["USERNAME"] = userName
        authParams["PASSWORD"] = tempPass

        val authRequest = AdminInitiateAuthRequest()
            .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
            .withClientId(Constants.COGNITO_CLIENT_ID)
            .withUserPoolId(Constants.USER_POOL_ID)
            .withAuthParameters(authParams)

        val result = cognitoClient.adminInitiateAuth(authRequest)

        if(result.challengeName == "NEW_PASSWORD_REQUIRED") {
            val challengeResponse = HashMap<String, String>()
            challengeResponse["USERNAME"] = userName
            challengeResponse["PASSWORD"] = tempPass
            challengeResponse["NEW_PASSWORD"] = password

            val updatePassRequest = AdminRespondToAuthChallengeRequest()
                .withChallengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
                .withChallengeResponses(challengeResponse)
                .withClientId(Constants.COGNITO_CLIENT_ID)
                .withUserPoolId(Constants.USER_POOL_ID)
                .withSession(result.session)

            val challengeResult = cognitoClient.adminRespondToAuthChallenge(updatePassRequest)

            return RepositoryResponse(200, "User $userName added successfully.")
        } else if(StringUtils.isNullOrEmpty(result.challengeName)) {
            return RepositoryResponse(200, "User $userName added successfully.")
        } else {
            return RepositoryResponse(400, "Failed to created user $userName")
        }
    }

    private fun createTempPass() : String {
        val passGenerator = PasswordGenerator()
        val lowerCaseRule = CharacterRule(EnglishCharacterData.LowerCase)
        lowerCaseRule.numberOfCharacters = 3

        val upperCaseRule = CharacterRule(EnglishCharacterData.UpperCase)
        upperCaseRule.numberOfCharacters = 3

        val digitRule = CharacterRule(EnglishCharacterData.Digit)
        digitRule.numberOfCharacters = 2

        return passGenerator.generatePassword(8, lowerCaseRule, upperCaseRule, digitRule)
    }

}