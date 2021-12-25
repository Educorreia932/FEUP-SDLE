package gnutella.peer

import gnutella.handlers.PingHandler
import gnutella.handlers.PongHandler
import gnutella.handlers.QueryHandler
import gnutella.handlers.QueryHitHandler
import gnutella.messages.*
import java.net.*
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

class MessageBroker(
    private val peer: Peer,
) {
    private val inbox = LinkedBlockingQueue<Message>()
    private val outbox = LinkedBlockingQueue<Message>()

    init {
        // Receive messages
        thread {
            val socket = DatagramSocket(peer.port)

            while (true) {
                val response = ByteArray(65536)
                val packet = DatagramPacket(response, response.size)

                socket.receive(packet)

                val data = packet.data.slice(0 until packet.length).toByteArray()
                val message = Message.fromBytes(data)

                println("Peer ${peer.username} | Received message ${message.content}")

                inbox.put(message)
            }
        }

        // Send messages
        thread {
            val socket = DatagramSocket()

            while (true) {
                val message = outbox.take()
                val payload = message.content

                println("Peer ${peer.username} | Sent message ${message.content}")

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
            when (val message = inbox.take()) {
                is Ping -> PingHandler(peer, message).run()
                is Pong -> PongHandler(peer, message).run()
                is Query -> QueryHandler(peer, message).run()
                is QueryHit -> QueryHitHandler(peer, message).run()
            }
        }
    }

    fun putMessage(message: Message) {
        outbox.put(message)
    }
}