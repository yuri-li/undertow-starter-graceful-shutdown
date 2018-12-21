package org.undertowx

import io.undertow.server.HandlerWrapper
import io.undertow.server.HttpHandler
import io.undertow.server.handlers.GracefulShutdownHandler
import org.springframework.stereotype.Component

@Component
class UndertowWrapper : HandlerWrapper {
    var gracefulShutdownHandler: GracefulShutdownHandler? = null

    override fun wrap(handler: HttpHandler): HttpHandler {
        if (gracefulShutdownHandler == null) {
            this.gracefulShutdownHandler = GracefulShutdownHandler(handler);
        }
        return gracefulShutdownHandler!!
    }
}