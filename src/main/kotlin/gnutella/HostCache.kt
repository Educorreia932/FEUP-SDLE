package gnutella

import User
import gnutella.messages.ConnectTo
import gnutella.messages.Message
import gnutella.messages.RequestConnect
import gnutella.peer.Neighbour
import gnutella.peer.Node
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.ServerSocket
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.thread

class HostCache {
    private val amountNeighbours = 3
    private val peers: MutableSet<Node> = ConcurrentHashMap.newKeySet()

    init {

        thread {
            val serverSocket = ServerSocket(Constants.HOST_CACHE_PORT)

            serverSocket.use {
                while (true) {
                    val socket = serverSocket.accept()

                    socket.use {
                        // Receive message from connected peer
                        var message: Message
                        val objectInputStream = ObjectInputStream(socket.getInputStream())

                        objectInputStream.use {
                            message = objectInputStream.readObject() as Message

                            if (message is RequestConnect) {
                                peers.add(message.source)

                                // Reply to peer with list of possible neighbours
                                val possibleNeighbours = takeNRandomNeighbours(amountNeighbours)
                                val objectOutputStream = ObjectOutputStream(socket.getOutputStream())

                                objectOutputStream.use {
                                    objectOutputStream.writeObject(
                                        ConnectTo(
                                            UUID.randomUUID(), Neighbour(
                                                User("host_cache"),
                                                Constants.HOST_CACHE_ADDRESS,
                                                Constants.HOST_CACHE_PORT
                                            ), possibleNeighbours
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun takeNRandomNeighbours(n: Int): Set<Node> {
        val ret = mutableSetOf<Node>()

        while (ret.size < n && ret.size < peers.size) {
            ret.add(peers.random())
        }

        return ret
    }

}
