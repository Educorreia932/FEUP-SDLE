package gnutella.peer

import gnutella.handlers.PingHandler
import gnutella.handlers.PongHandler
import gnutella.handlers.QueryHandler
import gnutella.handlers.QueryHitHandler
import gnutella.messages.*
import java.io.*
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
            val serverSocket = ServerSocket(peer.port)

            while (true) {
                val socket = serverSocket.accept()
                thread {

                    socket.use {
                        val objectInputStream = ObjectInputStream(socket.getInputStream())
                        objectInputStream.use {
                            val message = objectInputStream.readObject() as Message

                            println("Peer ${peer.user.username} | Received message $message")

                            inbox.put(message)

                        }
                    }
                }
            }
        }

        // Send messages
        thread {
            while (true) {
                val message = outbox.take()

                val socket = Socket(
                    message.destination!!.address,
                    message.destination!!.port
                )
                
                socket.use {
                    val objectOutputStream = ObjectOutputStream(socket.getOutputStream())
                    objectOutputStream.use {
                        println("Peer ${peer.user.username} | Sent $message to Peer ${message.destination?.user?.username}")

                        objectOutputStream.writeObject(message)
                    }
                }
            }
        }

        // Process messages
        thread {
            while (true) {
                when (val message = inbox.take()) {
                    is Ping -> PingHandler(peer, message).run()
                    is Pong -> PongHandler(peer, message).run()
                    is Query -> QueryHandler(peer, message).run()
                    is QueryHit -> QueryHitHandler(peer, message).run()
                }
            }
        }
    }

    fun putMessage(message: Message) {
        outbox.put(message)
    }
}