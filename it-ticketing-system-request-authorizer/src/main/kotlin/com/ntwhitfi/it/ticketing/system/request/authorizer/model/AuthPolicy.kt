package com.ntwhitfi.it.ticketing.system.request.authorizer.model

import com.ntwhitfi.it.ticketing.system.request.authorizer.config.Constants

class AuthPolicy {
    enum class HttpMethod {
        GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS, ALL
    }

    var principalId: String = ""
    @Transient var policyDocumentObject:AuthPolicy.PolicyDocument? = null

    constructor(principalId: String, policyDocumentObject: AuthPolicy.PolicyDocument) {
        this.principalId = principalId
        this.policyDocumentObject = policyDocumentObject
    }

    class PolicyDocument {
        val EXECUTE_API_ARN_FORMAT: String = "arn:aws:execute-api:%s:%s:%s/%s/%s/%s"
        val Version = "2012-10-17"

        var allowStatement: Statement? = null
        //var statementList: ArrayList<Statement?> = ArrayList<Statement?>()

        @Transient var region: String? = ""
        @Transient var awsAccountId: String? = ""
        @Transient var restApiId: String? = ""
        @Transient var stage: String? = ""

        constructor(region: String, awsAccountId: String, restApiId: String, stage: String) {
            getAllowOnePolicy(region, awsAccountId, restApiId, stage, HttpMethod.ALL, "*")
        }

        companion object {
            @JvmStatic
            fun getAllowOnePolicy(region: String?, awsAccountId: String?, restApiId: String?, stage: String?, method: HttpMethod?, resourcePath: String?): PolicyDocument {
                val policyDocument = PolicyDocument(region!!, awsAccountId!!, restApiId!!, stage!!)
                policyDocument.allowMethod(method, resourcePath)
                return policyDocument
            }
        }

        fun allowMethod(httpMethod: HttpMethod?, resourcePath: String?) {
            addResourceToStatement(allowStatement!!, httpMethod!!, resourcePath!!)
        }

        private fun addResourceToStatement(statement: Statement, httpMethod: HttpMethod, tempResourcePath: String) {
            var resourcePath = tempResourcePath
            if (resourcePath == "/") {
                resourcePath = ""
            }
            val resource = if (resourcePath.startsWith("/")) {
                resourcePath.substring(1)
            } else {
                resourcePath
            }

            val method = if (httpMethod === HttpMethod.ALL) {
                "*"
            } else {
                httpMethod.toString()
            }
            statement.addResource(
                String.format(EXECUTE_API_ARN_FORMAT, region, awsAccountId, restApiId, stage, method, resource)
            )
        }
    }

    class Statement {
        var Effect: String? = ""
        var Action: String? = ""
        var Condition: Map<String, Map<String, Object>>? = null

        var resourceList: ArrayList<String> = ArrayList<String>()

        constructor(effect: String, action: String, resourceList: ArrayList<String>, condition: Map<String, Map<String, Object>>?) {
            this.Effect = effect
            this.Action = action
            this.resourceList = resourceList
            this.Condition = condition
        }

        fun addResource(resource: String?) {
            resourceList.add(resource!!)
        }
    }
}