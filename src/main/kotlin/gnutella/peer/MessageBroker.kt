package gnutella.peer

import User
import gnutella.Constants
import gnutella.handlers.PingHandler
import gnutella.handlers.PongHandler
import gnutella.handlers.QueryHandler
import gnutella.handlers.QueryHitHandler
import gnutella.messages.*
import gnutella.myMessagePort
import gnutella.myPeerId
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.*
import java.nio.charset.StandardCharsets
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

                println("Peer ${peer.user.username} | Received message $message")

                inbox.put(message!!)
            }
        }

        // Send messages
        thread {
            val socket = DatagramSocket()

            while (true) {
                val message = outbox.take()
                val payload = message.toBytes()

                println("Peer ${peer.user.username} | Sent $message to ${message.destinationAddress}:${message.destinationPort}")

                val packet = DatagramPacket(
                    payload,
                    payload.size,
                    InetAddress.getByName(message.destinationAddress),
                    message.destinationPort!!
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

        // Accept incoming (TCP) connection requests, then accept the connection message.
        thread {
            val serverSock = ServerSocket(peer.port + 1)
            while (true){
                val newSock = serverSock.accept()
                newSock.soTimeout = Constants.CONNECTION_TIMEOUT_MILIS

                val inputStream = DataInputStream(newSock.getInputStream())
                var stringReceived = ""
                try {
                    stringReceived = inputStream.readUTF()
                }
                catch (exception: SocketTimeoutException){
                    println("Too late.")
                    inputStream.close()
                    newSock.close()
                    continue
                }

                val splitStr = stringReceived.split(Constants.CONNECTION_MESSAGE_SEPARATOR)
                if(splitStr.size != 4){
                    println("Peer tried to connect using an invalid mesasge. Exiting.")
                    continue
                }

                if(splitStr[0].equals(Constants.CONNECTION_REQUEST_STRING)){
                    val outputStream = DataOutputStream(newSock.getOutputStream())
                    outputStream.writeUTF(Constants.CONNECTION_ACCEPTANCE_STRING + Constants.CONNECTION_MESSAGE_SEPARATOR + peer.address + Constants.CONNECTION_MESSAGE_SEPARATOR + peer.port + Constants.CONNECTION_MESSAGE_SEPARATOR + peer.user.username)
                    outputStream.close()
                    peer.addNeighbour(Neighbour(User(splitStr[3]), port = splitStr[2].toInt()))
                }
            }
        }
    }

    fun putMessage(message: Message) {
        println("Put message to outbox")
        outbox.put(message)
    }
}