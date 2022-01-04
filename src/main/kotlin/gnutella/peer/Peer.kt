package gnutella.peer

import User
import gnutella.Constants
import gnutella.messages.Message
import gnutella.messages.Ping
import gnutella.messages.Query
import java.util.*

/**
 * Representation of a Gnutella node
 */
class Peer(
    val user: User,
    val address: String = "127.0.0.1",
    val port: Int,
) {
    private val neighbours = mutableSetOf<Neighbour>()
    private val messageBroker = MessageBroker(this)
    val cache = Cache(this)
    val storage = Storage()

    fun addNeighbour(address: String, port: Int) {
        addNeighbour(Neighbour(User(""), address, port))
    }

    fun addNeighbour(neighbour: Neighbour) {
        if ((!neighbour.sameAsPeer(this)) && neighbour !in neighbours)
            neighbours.add(neighbour)
    }

    fun addNeighbour(peer: Peer) {
        val neighbour = Neighbour(peer)
        addNeighbour(neighbour)
    }

    // Test function; Prints all neighbours' info
    fun printNeighbours() {
        println("Neighbours: ")
        for (i in neighbours) {
            println("Username: ${i.user.username}; Address: ${i.address}; Port: ${i.port}")
        }
    }

    fun removeNeighbour(address: String, port: Int) {
        neighbours.remove(Neighbour(User(""), address, port))
    }

    fun removeNeighbour(neighbour: Neighbour) {
        if ((!neighbour.sameAsPeer(this)) && neighbour in neighbours)
            neighbours.remove(neighbour)
    }

    fun ping() {
        val message = Ping(UUID.randomUUID(), Constants.MAX_HOPS, 0)

        forwardMessage(message, null)
    }

    fun search(keyword: String) {
        val message = Query(UUID.randomUUID(), Constants.MAX_HOPS, 0, keyword)

        forwardMessage(message, null)
    }

    fun sendMessage(message: Message, address: String, port: Int) {
        val msg = message.to(address, port)
        messageBroker.putMessage(message.to(address, port))
    }

    fun sendMessage(message: Message, neighbour: Neighbour) {
        val msg = message.to(neighbour)
        messageBroker.putMessage(msg)
    }

    fun forwardMessage(message: Message, neighbourToExclude: Neighbour?) {
        for (neighbour in neighbours)
            if (neighbour != neighbourToExclude)
                sendMessage(message, neighbour)
    }

    override fun equals(other: Any?): Boolean {
        return user.username == (other as Peer).user.username
    }

    override fun hashCode(): Int {
        return user.username.hashCode()
        // return  31 * result + neighbours.hashCode()
    }
}