package bandwidthBroker

import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.gson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.http.*


class App {
    fun main() {
        embeddedServer(Netty, port = 8080) { module() }.start(wait = true)
    }

    fun Application.module() {
        install(ContentNegotiation) { gson { setPrettyPrinting() } }

        routing {
            get("/") { call.respondText("Hello, World!") }

            get("/api") { call.respond(HttpStatusCode.OK, mapOf("message" to "Hello from Ktor!")) }

            post("/api") {
                val post = call.receive<Map<String, String>>()
                call.respond(HttpStatusCode.OK,mapOf("received" to post))
            }
            get("/notfound") {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Resource not found"))
            }

            post("/create") {
                val post = call.receive<Map<String, String>>()
                call.respond(HttpStatusCode.Created, mapOf("created" to post))
            }

            get("/unauthorized") {
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Unauthorized access"))
            }

            get("/custom") {
                call.respond(HttpStatusCode(418, "I'm a teapot"), mapOf("message" to "I'm a teapot"))
            }
        }
    }
}
