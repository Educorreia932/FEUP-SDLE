package gnutella

import social.User
import gnutella.messages.ConnectTo
import gnutella.messages.Message
import gnutella.messages.RequestConnect
import gnutella.peer.Neighbour
import gnutella.peer.Node
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.ServerSocket
import java.net.Socket
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
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

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(
            { randomCheckAlive() },
            5,
            1,
            TimeUnit.SECONDS
        )

//        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(
//            { println(peers.size) },
//            6,
//            3,
//            TimeUnit.SECONDS
//        )
    }

    private fun randomCheckAlive() {
        for (node in takeNRandomNeighbours(Constants.MAX_NEIGHBOURS)) {
            var alive = true
            for (i in 0..5) {
                alive = try {
                    val socket = Socket(
                        node.address, node.port
                    )
//                    socket.use {
//                        val objectOutputStream = ObjectOutputStream(socket.getOutputStream())
//                        objectOutputStream.close()
//                    }
                    socket.close()
                    true
                } catch (_: Exception) {
                    false
                }

                if (alive)
                    break
            }

            if (!alive){
                peers.remove(node) // Assume dead
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
