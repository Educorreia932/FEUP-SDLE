package gnutella

import Post
import User
import gnutella.peer.Peer
import org.graphstream.graph.Graph
import org.graphstream.graph.implementations.SingleGraph
import java.util.*

fun main() {
    System.setProperty("org.graphstream.ui", "swing")

    val peers = mutableListOf<Peer>()
    val graph: Graph = SingleGraph("Network")
    HostCache()

    graph.display()

    for (i in 1..10) {
        val peer = Peer(User(i.toString()), graph = graph)
        peer.connect()
        peers.add(peer)
    }

    peers[9].user.follow(peers[3].user)

    peers[3].storage.addPost(Post(UUID.randomUUID(), "Rãs", peers[3].user))
    peers[9].search("4")
    //sleep(15000)
    //println("Second search start: ")
    //peers[9].search("4")

}