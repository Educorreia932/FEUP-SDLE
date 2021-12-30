package gnutella.peer

import User
import gnutella.Constants
import gnutella.messages.Message
import gnutella.messages.Ping
import gnutella.messages.Query

/**
 * Representation of a Gnutella node
 */
class Neighbour{
    val user: User
    var address: String = "127.0.0.1"
    val port: Int

    constructor(user: User, address: String = "127.0.0.1", port: Int){
        this.user = user
        this.address = address
        this.port = port
    }

    constructor(peer: Peer){
        user = peer.user
        address = peer.address
        port = peer.port
    }

    override fun equals(other: Any?): Boolean {
        return user.username == (other as Neighbour).user.username
    }

    override fun hashCode(): Int {
        var result = user.username.hashCode()
        //result = 31 * result + neighbours.hashCode()

        return result
    }

    fun sameAsPeer(peer: Peer): Boolean{
        return user.username == peer.user.username
    }

}