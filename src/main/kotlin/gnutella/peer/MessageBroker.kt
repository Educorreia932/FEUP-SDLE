package gnutella.peer

import gnutella.handlers.PingHandler
import gnutella.handlers.PongHandler
import gnutella.handlers.QueryHandler
import gnutella.handlers.QueryHitHandler
import gnutella.messages.Message
import java.net.*
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

class MessageBroker(
    private val peer: Peer,
    address: String,
    port: Int
) {
    private val inbox = LinkedBlockingQueue<Message>()
    private val outbox = LinkedBlockingQueue<Message>()
    private val socket = DatagramSocket()

    init {
        // Receive messages
        thread {
            while (true) {
                val response = ByteArray(65536)
                val packet = DatagramPacket(response, response.size)

                socket.receive(packet)

                val message = Message(address, port, response.toString())

                println(String(response))

                inbox.put(message)
            }
        }

        // Send messages
        thread {
            while (true) {
                val message = outbox.take()
                val payload = message.content

                val packet = DatagramPacket(
                    payload.toByteArray(),
                    payload.length,
                    InetAddress.getByName(message.address),
                    message.port
                )

                socket.send(packet)
            }
        }

        // Process messages
        thread {
            val message = inbox.take()

            when (message.content) {
                "PING" -> PingHandler(peer, message).run()
                "PONG" -> PongHandler(peer, message).run()
                "QUERY" -> QueryHandler(peer, message).run()
                "QUERY_HIT" -> QueryHitHandler(peer, message).run()
            }
        }
    }

    fun putMessage(message: Message) {
        outbox.put(message)
    }
}