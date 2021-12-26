package gnutella.peer

import User
import gnutella.messages.Message
import gnutella.messages.Ping
import gnutella.messages.Query

/**
 * Representation of a Gnutella node
 */
class Peer(
    val user: User,
    val address: String = "127.0.0.1",
    val port: Int,
) {
    private val neighbours = mutableSetOf<Peer>()
    private val messageBroker = MessageBroker(this)
    val storage = Storage()

    fun addNeighbour(address: String, port: Int) {
        addNeighbour(Peer(User(""), address, port))
    }

    fun addNeighbour(peer: Peer) {
        if (this != peer && peer !in neighbours)
            neighbours.add(peer)
    }

    fun removeNeighbour(address: String, port: Int) {
        neighbours.remove(Peer(User(""), address, port))
    }

    fun removeNeighbour(peer: Peer) {
        if (this != peer && peer in neighbours)
            neighbours.remove(peer)
    }

    fun ping() {
        val message = Ping(address, port)

        forwardMessage(message)
    }

    fun search(keyword: String) {
        val message = Query(address, port, keyword)

        forwardMessage(message)
    }

    fun sendMessage(message: Message, address: String, port: Int) {
        messageBroker.putMessage(message.to(address, port))
    }
    
    fun sendMessage(message: Message, peer: Peer) {
        messageBroker.putMessage(message.to(peer))
    }

    fun forwardMessage(message: Message) {
        for (neighbour in neighbours)
            sendMessage(message, neighbour)
    }

    override fun equals(other: Any?): Boolean {
        return user.username == (other as Peer).user.username
    }

    override fun hashCode(): Int {
        var result = user.username.hashCode()
        result = 31 * result + neighbours.hashCode()

        return result
    }
}