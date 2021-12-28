package gnutella

import Post
import User
import gnutella.peer.Peer

fun main() {
    val peers = mutableListOf<Peer>()

    for (i in 0..4)
        peers.add(Peer(User(i.toString()), port = 8000 + i))

    // Connect peers in star pattern
    peers[0].addNeighbour(peers[2])
    peers[0].addNeighbour(peers[4])
    peers[1].addNeighbour(peers[3])
    peers[1].addNeighbour(peers[4])
    peers[2].addNeighbour(peers[4])

    peers[3].storage.addPost(Post("Rãs", peers[3].user))

    peers[0].search("4")
}