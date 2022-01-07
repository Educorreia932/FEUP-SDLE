package gnutella

import Post
import User
import gnutella.peer.Peer

fun main() {
    val peers = mutableListOf<Peer>()

    // Contains which peers (key) are connected to which peers (values)
    var connectionMap: MutableMap<Int, Int> = mutableMapOf()

    // 1 - 0; 3 - 1; 2 - 1
    // 0 - 1; 1 - 3; 1 - 2
    connectionMap[1] = 0
    connectionMap[3] = 1
    connectionMap[2] = 1
    connectionMap[0] = 0
    connectionMap[3] = 1
    connectionMap[2] = 1

    peers.add(Peer(User("0"), "127.0.0.1", port = 8020, "127.0.0.1", 8021))
    for(i in 1..3){
        println(i)
        if(connectionMap[i] != null){
            val peer = Peer(User(i.toString()), "127.0.0.1", port = 8020 + 2 * i, "127.0.0.1", 8021 + 2 * i, "127.0.0.1", 8021 + 2 * connectionMap[i]!!)
            peers.add(peer)
        }
        else{
            //Create unconnected peer so the indices still match the username
            val peer = Peer(User(i.toString()), "127.0.0.1", port = 8020 + 2 * i, "127.0.0.1", 8021 + 2 * i)
            peers.add(peer)
        }
    }


    /*
    peers.add(Peer(User(0.toString()), "127.0.0.1", port = 8020, "127.0.0.1", 8021))
    peers.add(Peer(User(1.toString()), address = "127.0.0.1", port = 8022, "127.0.0.1", 8023, "127.0.0.1", 8021))
    peers.add(Peer(User(2.toString()), address = "127.0.0.1", port = 8024, "127.0.0.1", 8025, "127.0.0.1", 8023))
    peers.add(Peer(User(3.toString()), address = "127.0.0.1", port = 8026, "127.0.0.1", 8027, "127.0.0.1", 8023))
    */

    peers[3].storage.addPost(Post("Rãs", peers[3].user))

    peers[0].search("3")
}