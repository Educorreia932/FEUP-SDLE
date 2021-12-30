package gnutella

import Post
import User
import gnutella.peer.Peer

fun main() {
    val peers = mutableListOf<Peer>()

    for (i in 0..4)
        peers.add(Peer(User(i.toString()), port = 8010 + (2 * i)))

    // Connect peers in star pattern
    peers[0].addNeighbour(peers[2])
    peers[2].addNeighbour(peers[0])

    //peers[0].addNeighbour(peers[3])
    //peers[3].addNeighbour(peers[0])

    peers[1].addNeighbour(peers[3])
    peers[3].addNeighbour(peers[1])

    peers[1].addNeighbour(peers[4])
    peers[4].addNeighbour(peers[1])

    peers[2].addNeighbour(peers[4])
    peers[4].addNeighbour(peers[2])

    peers[3].storage.addPost(Post("Rãs", peers[3].user))

    peers[0].search("4")
}