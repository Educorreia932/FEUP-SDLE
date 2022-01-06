package gnutella.peer

import User
import gnutella.Constants
import gnutella.connection.ConnectionMessage
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
    private var connectionAcceptSocket: ServerSocket? = null

    init {
        // Receive messages
        thread {
            val socket = DatagramSocket(peer.port)

            while (true) {
                val response = ByteArray(65536)
                val packet = DatagramPacket(response, response.size)

                socket.receive(packet)


                val data = packet.data.slice(0 until packet.length).toByteArray()
                val byteArrayInputStream = ByteArrayInputStream(data)
                val objectInputStream = ObjectInputStream(byteArrayInputStream)
                val message = objectInputStream.readObject() as Message

                objectInputStream.close()

                println("Peer ${peer.user.username} | Received message $message")

                inbox.put(message)
            }
        }

        // Send messages
        thread {
            val socket = DatagramSocket()

            while (true) {
                val message = outbox.take()
                val byteArrayOutputStream = ByteArrayOutputStream()
                val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)

                objectOutputStream.writeObject(message)

                val payload = byteArrayOutputStream.toByteArray()

                objectOutputStream.close()

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
            while (true) {
                when (val message = inbox.take()) {
                    is Ping -> PingHandler(peer, message, Neighbour(User(""), port = -1)).run()
                    is Pong -> PongHandler(peer, message, Neighbour(User(""), port = -1)).run()
                    is Query -> QueryHandler(peer, message, Neighbour(User(""), port = -1)).run()
                    is QueryHit -> QueryHitHandler(peer, message, Neighbour(User(""), port = -1)).run()
                }
            }
        }


    }

    constructor(peer: Peer, serverSocketAddress: String, serverSocketPort: Int): this(peer){
        connectionAcceptSocket = ServerSocket(serverSocketPort)
        receiveConnectionsThread()
    }

    public fun setConnectionAcceptSocket(socket: ServerSocket){
        connectionAcceptSocket = socket
    }

    public fun createConnectionAcceptSocket(serverSocketAddress: String, serverSocketPort: Int){
        connectionAcceptSocket = ServerSocket(serverSocketPort)
        receiveConnectionsThread()
    }

    fun putMessage(message: Message) {
        println("Put message to outbox")
        outbox.put(message)
    }

    fun receiveConnectionsThread(){
        // Accept incoming (TCP) connection requests, then accept the connection message.
        thread {
            while (true) {
                val newSock = connectionAcceptSocket!!.accept()
                newSock.soTimeout = Constants.CONNECTION_TIMEOUT_MILIS

                val inputStream = DataInputStream(newSock.getInputStream())
                var stringReceived: String

                try {
                    stringReceived = inputStream.readUTF()
                } catch (exception: SocketTimeoutException) {
                    println("Too late.")
                    inputStream.close()
                    newSock.close()
                    continue
                }

                val splitStr = stringReceived.split(Constants.CONNECTION_MESSAGE_SEPARATOR)
                if (splitStr.size != 4) {
                    println("Peer tried to connect using an invalid message. Exiting.")
                    continue
                }

                if (splitStr[0] == Constants.CONNECTION_REQUEST_STRING) {
                    val outputStream = DataOutputStream(newSock.getOutputStream())
                    outputStream.writeUTF(
                        ConnectionMessage.getConnAcceptMsg(
                            peer.address,
                            peer.port,
                            peer.user.username
                        )
                    )
                    outputStream.close()
                    peer.addNeighbour(Neighbour(User(splitStr[3]), port = splitStr[2].toInt()))
                    peer.printNeighbours()
                }
            }
        }
    }
}