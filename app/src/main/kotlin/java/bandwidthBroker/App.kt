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
        //public void allocateResources(String emitterNetwork, String receiverNetwork,
			String //type, double asked) throws NotEnoughResourcesException {
            post("/reservation") {
                val requestBody = call.receive<Map<String, Any>>()
                val emitter = requestBody["emitter"] as? String
                val receiver = requestBody["receiver"] as? String
                val type = requestBody["type"] as? String
                val bandwidth = requestBody["bandwidth"] as? Double

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
