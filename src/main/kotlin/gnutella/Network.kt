package gnutella

import Post
import User
import gnutella.peer.Peer

fun main() {
    val peers = mutableListOf<Peer>()

    /*
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
    peers[4].addNeighbour(peers[2])*/
    peers.add(Peer(User(0.toString()), "127.0.0.1", port = 8010, "127.0.0.1", 8011))
    peers.add(Peer(User(1.toString()), address = "127.0.0.1", port = 8012, "127.0.0.1", 8013, "127.0.0.1", 8011))
    peers.add(Peer(User(2.toString()), address = "127.0.0.1", port = 8014, "127.0.0.1", 8015, "127.0.0.1", 8013))
    peers.add(Peer(User(3.toString()), address = "127.0.0.1", port = 8016, "127.0.0.1", 8017, "127.0.0.1", 8013))

    peers[3].storage.addPost(Post("Rãs", peers[3].user))

    peers[0].search("4")
}