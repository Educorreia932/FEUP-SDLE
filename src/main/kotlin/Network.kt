package gnutella

import social.User
import gnutella.peer.Peer
import gui.GUI
import org.graphstream.graph.Graph
import org.graphstream.graph.implementations.SingleGraph
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun main() {
    System.setProperty("org.graphstream.ui", "swing")

    val peers = mutableListOf<Peer>()
    val graph: Graph = SingleGraph("Network")
    HostCache()

    graph.display()

    for (i in 1..50) {
        val peer = Peer(User(i.toString()), graph = graph)

        peer.connect()
        peers.add(peer)
    }

    peers[7].user.follow(peers[3].user)

    peers[3].user.createPost("Sapos")
    peers[3].user.createPost("Rãs")
    peers[3].user.createPost("Sapinhos")

    peers[7].search("4")

//    Executors.newScheduledThreadPool(1).scheduleAtFixedRate(
//        {
//            val node = peers.random()
//            node.stop()
//            peers.remove(node)
//        }, 7, 5, TimeUnit.SECONDS
//    )

    Thread.sleep(200)

    peers[5].user.createPost("bruh aaa")
    peers[5].user.createPost("bruh bbb")

    val gui = GUI(peers)

    gui.start()
    Thread.sleep(5000)
    peers[4].discover("user:4 bruh")
}