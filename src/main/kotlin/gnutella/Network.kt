package gnutella

import Post
import User
import gnutella.peer.Peer
import gui.GUI
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

    peers[7].user.follow(peers[3].user)
    
    peers[3].storage.addPost(Post(UUID.randomUUID(), "Rãs", peers[3].user))
    peers[7].search("4")
    
    Thread.sleep(200)
    
    val gui = GUI(peers)
    
    gui.start()
}