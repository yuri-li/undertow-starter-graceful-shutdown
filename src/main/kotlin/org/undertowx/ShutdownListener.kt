package org.undertowx

import com.ecwid.consul.v1.ConsulClient
import com.ecwid.consul.v1.catalog.CatalogServiceRequest
import com.ecwid.consul.v1.catalog.model.CatalogDeregistration
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextClosedEvent
import org.springframework.stereotype.Component

@Component
class ShutdownListener(val consul: ConsulClient, val wrapper: UndertowWrapper, @Value("\${spring.application.name}") val serviceName: String) : ApplicationListener<ContextClosedEvent> {
    val log = LoggerFactory.getLogger(this.javaClass)
    @Volatile var isShutdown = false

    override fun onApplicationEvent(event: ContextClosedEvent) {
        if (!isShutdown) {
            deRegister()
            isShutdown = true
            GlobalScope.launch {
                shutdown()
            }
        }
    }

    private fun deRegister() {
        val config = this.consul.agentSelf.value.config
        val catalogServiceRequest: CatalogServiceRequest? = null
        val service = consul.getCatalogService(serviceName, catalogServiceRequest).value.filter { it.node.equals(config.nodeName) }.first()
        val obj = CatalogDeregistration()
        obj.node = service.getNode()
        obj.serviceId = service.getServiceId()
        consul.catalogDeregister(obj)
    }

    fun shutdown() {
        wrapper.gracefulShutdownHandler!!.shutdown()
        try {
            wrapper.gracefulShutdownHandler!!.awaitShutdown(30000)
        } catch (e: InterruptedException) {
            log.error(e.toString())
        }
    }
}