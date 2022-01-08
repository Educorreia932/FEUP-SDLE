package gnutella

import java.io.File
import gnutella.peer.Peer
import User
import org.graphstream.graph.Graph
import org.graphstream.graph.implementations.SingleGraph

fun main() {
    val graph: Graph = SingleGraph("Network")

    graph.display()

    val peers: MutableMap<String, Peer> = mutableMapOf()

    File("test.txt").forEachLine {
        println(it)
        val args = it.split(" ")
        val origin = args[0]
        when (origin) {
            "PEER" -> {
                val username: String = args[1]
                val action = args[2]
                when (action) {
                    "CONNECT" -> {
                        when (args.size) {
                            3 -> {
                                val peer = Peer(User(username), graph = graph)
                                peers[username] = peer
                                peer.connect()
                            }
                        }
                    }
                    "SEARCH" -> {
                        peers[username]?.search(args[2])
                    }
                    "POST" -> {
                        println(action + "not implemented yet.")
                    }
                }
            }
            else -> {
                print("Option " + origin + " does not exist.")
                return@forEachLine;
            }
        }
    }
}
/*
PEER 1 CONNECT
PEER 2 CONNECT 1
PEER 1 SEARCH a
PEER 2 DISCONNECT
*/