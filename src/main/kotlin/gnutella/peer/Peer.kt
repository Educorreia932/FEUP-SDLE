package gnutella.peer

import User
import gnutella.Constants
import gnutella.messages.Message
import gnutella.messages.Ping
import gnutella.messages.Query
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.*

/**
 * Representation of a Gnutella node
 */
class Peer(
    user: User,
    address: String = "127.0.0.1",
    port: Int, // TODO: Get a free port, if none is specified
) : Node(user, InetAddress.getByName(address), port) {
    @Transient
    val neighbours = mutableSetOf<Neighbour>()

    @Transient
    private val messageBroker = MessageBroker(this)

    @Transient
    val cache = Cache(this)

    @Transient
    val storage = Storage()

    fun addNeighbour(address: InetAddress, port: Int) {
        addNeighbour(Neighbour(User(""), address, port))
    }

    fun addNeighbour(peer: Peer) {
        addNeighbour(Neighbour(peer))
    }

    private fun addNeighbour(neighbour: Neighbour) {
        if ((!neighbour.sameAsPeer(this)) && neighbour !in neighbours)
            neighbours.add(neighbour)
    }

    fun removeNeighbour(neighbour: Neighbour) {
        if ((!neighbour.sameAsPeer(this)) && neighbour in neighbours)
            neighbours.remove(neighbour)
    }

    fun ping() {
        val message = Ping(UUID.randomUUID(), this, user.username, Constants.MAX_HOPS, 0)

        forwardMessage(message)
    }

    fun search(keyword: String) {
        val message = Query(UUID.randomUUID(), this, user.username, Constants.MAX_HOPS, 0, keyword)

        forwardMessage(message)
    }

    fun sendMessage(message: Message, node: Node) {
        messageBroker.putMessage(message.to(node))
    }

    private fun forwardMessage(message: Message) {
        for (neighbour in neighbours)
//            if (neighbour != message.sender)
            sendMessage(message, neighbour)
    }

    fun forwardMessage(message: Message, propagator: String) {
        for (neighbour in neighbours) {
            if (neighbour.user.username != propagator)
                sendMessage(message, neighbour)
        }
    }

    override fun equals(other: Any?): Boolean {
        return user.username == (other as Peer).user.username
    }

    override fun hashCode(): Int {
        return user.username.hashCode()
    }
}