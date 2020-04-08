package main.kotlin.com.ntwhitfi.it.ticketing.system.request.authorizer.config

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RequestAuthorizerModule::class] )
interface RequestAuthorizerComponent {
}