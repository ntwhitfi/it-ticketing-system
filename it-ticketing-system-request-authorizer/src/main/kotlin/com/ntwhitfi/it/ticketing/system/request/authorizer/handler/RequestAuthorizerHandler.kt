package com.ntwhitfi.it.ticketing.system.request.authorizer.handler

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.ntwhitfi.it.ticketing.system.request.authorizer.model.AuthPolicy
import com.ntwhitfi.it.ticketing.system.request.authorizer.model.AuthPolicy.PolicyDocument
import com.ntwhitfi.it.ticketing.system.request.authorizer.model.AuthorizationRequest
import main.kotlin.com.ntwhitfi.it.ticketing.system.request.authorizer.config.DaggerRequestAuthorizerComponent
import main.kotlin.com.ntwhitfi.it.ticketing.system.request.authorizer.config.RequestAuthorizerComponent
import main.kotlin.com.ntwhitfi.it.ticketing.system.request.authorizer.model.RequestAuthorizerResponse
import main.kotlin.com.ntwhitfi.it.ticketing.system.request.authorizer.service.IRequestAuthorizer


class RequestAuthorizerHandler: RequestHandler<AuthorizationRequest, AuthPolicy> {
    lateinit var requestAuthorizer: IRequestAuthorizer

    constructor() {
        RequestAuthorizerHandler(DaggerRequestAuthorizerComponent.builder().build())
    }

    constructor(requestAuthorizerComponent: RequestAuthorizerComponent) {
        RequestAuthorizerHandler(requestAuthorizerComponent)
    }

    constructor(requestAuthorizer: IRequestAuthorizer) {
        this.requestAuthorizer = requestAuthorizer
    }

    override fun handleRequest(input: AuthorizationRequest, context: Context?): AuthPolicy? {
        val token: String = input.authorizationToken

        val authorizationResponse: RequestAuthorizerResponse = requestAuthorizer.validateAuthToken(token)

        if(authorizationResponse.statusCode == 200) {
            val principalId: String = requestAuthorizer.getJWTPrincipalId(token)

            val methodArn: String = input.methodArn
            val arnPartials = methodArn.split(":").toTypedArray()
            val region = arnPartials[3]
            val awsAccountId = arnPartials[4]
            val apiGatewayArnPartials = arnPartials[5].split("/").toTypedArray()
            val restApiId = apiGatewayArnPartials[0]
            val stage = apiGatewayArnPartials[1]
            //val httpMethod = apiGatewayArnPartials[2]
            var resource = "" // root resource
            if (apiGatewayArnPartials.size == 4) {
                resource = apiGatewayArnPartials[3]
            }
            return AuthPolicy(
                principalId,
                PolicyDocument.getAllowOnePolicy(region, awsAccountId, restApiId, stage, AuthPolicy.HttpMethod.ALL, resource)
            )

        } else {
            return null
        }
    }

}