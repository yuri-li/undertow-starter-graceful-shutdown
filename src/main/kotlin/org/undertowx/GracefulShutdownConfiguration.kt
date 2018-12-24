package org.undertowx

import io.undertow.servlet.api.DeploymentInfo
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.boot.web.embedded.undertow.UndertowDeploymentInfoCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class GracefulShutdownConfiguration(val wrapper: UndertowWrapper, val listener: ShutdownListener) {
    @Bean
    fun undertowCustomizer() = object : UndertowDeploymentInfoCustomizer {
        override fun customize(deploymentInfo: DeploymentInfo): Unit {
            deploymentInfo.addOuterHandlerChainWrapper(wrapper)
        }
    }

    @Bean
    fun healthIndicator() = object : HealthIndicator {
        override fun health(): Health = if (listener.isShutdown) {
            Health.outOfService().build()
        } else {
            Health.up().build()
        }
    }
}

