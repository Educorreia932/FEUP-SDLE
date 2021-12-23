package gnutella.peer

import gnutella.messages.Message
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread


class MessageBroker(
    address: String,
    port: Int
) {
    private val inbox = LinkedBlockingQueue<Message>()
    private val outbox = LinkedBlockingQueue<Message>()
    private val socket = MulticastSocket(port)

    init {
        socket.joinGroup(InetAddress.getByName(address))
        
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
                "PING" -> TODO()
                "PONG" -> TODO()
                "QUERY" -> TODO()
                "QUERY_HIT" -> TODO()
            }
        }
    }

    fun putMessage(message: Message) {
        outbox.put(message)
    }
}