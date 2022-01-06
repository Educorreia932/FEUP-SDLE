package gnutella

import Post
import User
import gnutella.peer.Peer

fun main() {
    val peers = mutableListOf<Peer>()

    // 1 - 0; 3 - 1; 2 - 1
    // 0 - 1; 1 - 3; 1 - 2
    peers.add(Peer(User(0.toString()), "127.0.0.1", port = 8020, "127.0.0.1", 8021))
    peers.add(Peer(User(1.toString()), address = "127.0.0.1", port = 8022, "127.0.0.1", 8023, "127.0.0.1", 8021))
    peers.add(Peer(User(2.toString()), address = "127.0.0.1", port = 8024, "127.0.0.1", 8025, "127.0.0.1", 8023))
    peers.add(Peer(User(3.toString()), address = "127.0.0.1", port = 8026, "127.0.0.1", 8027, "127.0.0.1", 8023))


    peers[3].storage.addPost(Post("Rãs", peers[3].user))

    peers[0].search("4")
}