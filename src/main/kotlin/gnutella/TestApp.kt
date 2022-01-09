package gnutella

import Post
import User
import gnutella.peer.Peer
import gui.GUI
import org.graphstream.graph.Graph
import org.graphstream.graph.implementations.SingleGraph
import java.io.File
import java.util.*

fun main() {
    System.setProperty("org.graphstream.ui", "swing")
    val graph: Graph = SingleGraph("Network")
    HostCache()
    graph.display()

    val peers: MutableMap<String, Peer> = mutableMapOf()

    System.setProperty("org.graphstream.ui", "swing")

    File("src/main/kotlin/gnutella/test.txt").forEachLine {
        println(it)
        val args = it.split(" ")
        when (val origin = args[0]) {
            "PEER" -> {
                val username: String = args[1]
                when (val action = args[2]) {
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
                        if(peers[username] == null){
                            println("Peer $username needs to connect before posting. [ '$it' ]")
                            return@forEachLine
                        }
                        peers[username]!!.storage.addPost(Post(UUID.randomUUID(), "Rãs", peers[username]!!.user))
                    }
                    "FOLLOW" ->{
                        if(args.size != 4){
                            println("$action can needs to have 4 args; has ${args.size} args instead. [ \'$it' ]")
                        }
                        if(peers[username] == null){
                            println("Peer $username needs to connect before following someone. [ '$it' ]")
                            return@forEachLine
                        }
                        peers[username]!!.user.follow(User(args[3]))

                    }
                }
            }
            else -> {
                print("Option $origin does not exist.")
                return@forEachLine
            }
        }
    }

    Thread.sleep(200)

    val gui = GUI(peers.values.toList())


    gui.start()
}
/*
PEER 1 CONNECT
PEER 2 CONNECT 1
PEER 1 SEARCH a
PEER 2 DISCONNECT
*/