package gnutella

import Post
import User
import gnutella.peer.Peer
import org.graphstream.graph.Graph
import org.graphstream.graph.implementations.SingleGraph

fun main() {
    System.setProperty("org.graphstream.ui", "swing")

    val peers = mutableListOf<Peer>()
    val graph: Graph = SingleGraph("Network")

    graph.display()

    for (i in 1..4)
        peers.add(Peer(User(i.toString()), graph = graph))

    peers[0].connect(peers[1])
    peers[1].connect(peers[2])
    peers[2].connect(peers[3])

    peers[3].storage.addPost(Post("Rãs", peers[3].user))

    peers[0].search("3")
}

 