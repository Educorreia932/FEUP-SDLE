package gnutella

import Post
import User
import gnutella.peer.Peer
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
//        Thread.sleep(100)
    }

    peers[7].user.follow(peers[3].user)

    peers[3].storage.addPost(Post(UUID.randomUUID(), "Rãs", peers[3].user))
    peers[3].storage.addPost(Post(UUID.randomUUID(), "Sapos", peers[3].user))

    peers[7].search("4")

//    Executors.newScheduledThreadPool(1).scheduleAtFixedRate(
//        {
//            val node = peers.random()
//            node.stop()
//            peers.remove(node)
//        }, 7, 5, TimeUnit.SECONDS
//    )

    Thread.sleep(200)

//    val gui = GUI(peers)

//    gui.start()
}