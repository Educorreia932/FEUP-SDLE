package gnutella

import Post
import User
import gnutella.peer.Peer
import java.util.*

class Network {
    val peers = mutableListOf<Peer>()

    init {
        peers.add(Peer(User("0"), port = 8001))
        peers.add(Peer(User("1"), port = 8002))
        peers.add(Peer(User("2"), port = 8003))
        peers.add(Peer(User("3"), port = 8004))

        peers[0].addNeighbour(peers[1])
        peers[1].addNeighbour(peers[2])
        peers[2].addNeighbour(peers[3])

        peers[3].storage.addPost(Post("Rãs", peers[3].user, Date()))

        peers[0].search("3")
    }
}
