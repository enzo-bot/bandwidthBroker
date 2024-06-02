package bandwidthBroker

import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

class App {
    var algo : QueryAlgorithm? = null;

    fun main() {
        
        //QueryAlgorithm algo = new QueryAlgorithm();
        this.algo = QueryAlgorithm(); 
        embeddedServer(Netty, port = 8080) { module() }.start(wait = true)
    }

    fun Application.module() {
        install(ContentNegotiation) { gson { setPrettyPrinting() } }

        routing {
            // .releaseResources("10.0.0.0/8","192.0.0.0/24","BE", 0.5);
            post("/free") {
                val requestBody = call.receive<Map<String, Any>>()
                println(requestBody)
                val emitter = requestBody["emitter"] as? String
                val receiver = requestBody["receiver"] as? String
                val type = requestBody["type"] as? String
                val bandwidth = requestBody["bandwidth"] as? Double
                println("Freeing $bandwidth Mb from $emitter to $receiver with $type")

                if( emitter == null || receiver == null || type == null || bandwidth == null) {
                    call.respond(
                            HttpStatusCode.BadRequest,
                            "Missing parameters. Please provide 'emitter', 'receiver', 'type', and 'bandwidth'."
                    )
                    return@post
                }
                try {
                    var receiver_network = NetworkPrefixExtractor.getNetworkPrefix(receiver,24)
                    var emitter_network = NetworkPrefixExtractor.getNetworkPrefix(receiver,24)
                    algo?.releaseResources(emitter_network, receiver_network, type, bandwidth)
                    
                } catch(e: Exception){
                    e.printStackTrace();
                    call.respond(
                            HttpStatusCode.BadRequest,
                            "Freeing failed with exception: " + e
                    )
                    return@post
                }

                val response = mapOf("Message" to "Successfully freed $bandwidth Mb")

                call.respond(HttpStatusCode.OK, response)
            }
            post("/reservation") {
                val requestBody = call.receive<Map<String, Any>>()
                println(requestBody)
                val emitter = requestBody["emitter"] as? String
                val receiver = requestBody["receiver"] as? String
                val type = requestBody["type"] as? String
                val bandwidth = requestBody["bandwidth"] as? Double
                println("Freeing $bandwidth Mb from $emitter to $receiver with $type")

                if( emitter == null || receiver == null || type == null || bandwidth == null) {
                    call.respond(
                            HttpStatusCode.BadRequest,
                            "Missing parameters. Please provide 'emitter', 'receiver', 'type', and 'bandwidth'."
                    )
                    return@post
                }
                try {
                    var receiver_network = NetworkPrefixExtractor.getNetworkPrefix(receiver,24)
                    var emitter_network = NetworkPrefixExtractor.getNetworkPrefix(receiver,24)
                    println("emitter net : " + emitter_network)
                    println("receiver net : " + receiver_network)
                    algo?.allocateResources(emitter_network, receiver_network, type, bandwidth)
                    
                } catch(e: Exception){
                    e.printStackTrace();
                    call.respond(
                            HttpStatusCode.BadRequest,
                            "AllocateResources failed with exception: " + e
                    )
                    return@post
                }

                val response = mapOf("Message" to "Successfully allocated $bandwidth Mb")

                call.respond(HttpStatusCode.OK, response)
            }
        }
    }
}
