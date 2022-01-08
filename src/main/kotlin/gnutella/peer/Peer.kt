package gnutella.peer

import User
import gnutella.Constants
import gnutella.messages.Message
import gnutella.messages.Ping
import gnutella.messages.Query
import org.graphstream.graph.Graph
import java.net.InetAddress
import java.net.ServerSocket
import java.util.UUID

/**
 * Representation of a Gnutella node
 */
class Peer(
    user: User,
    address: String = "127.0.0.1",
    port: Int = freePort(),
    @Transient
    val graph: Graph
) : Node(user, InetAddress.getByName(address), port) {
    @Transient
    private val routingTable = RoutingTable(this, graph)

    @Transient
    private val messageBroker = MessageBroker(this)

    @Transient
    val cache = Cache(this)

    @Transient
    val storage = Storage()

    init {
        val node = graph.addNode(port.toString())

        node.setAttribute("ui.label", "Peer ${user.username}")
    }

    fun connect(peer: Peer) {
        routingTable.addNeighbour(peer)

        ping()
    }

    private fun ping() {
        val message = Ping(UUID.randomUUID(), this, this, Constants.TTL, Constants.MAX_HOPS)

        routingTable.forwardMessage(message)
    }

    fun search(keyword: String) {
        val message = Query(UUID.randomUUID(), this, this, Constants.MAX_HOPS, 0, keyword)

        routingTable.forwardMessage(message)
    }

    fun forwardMessage(message: Message, propagator: Node) {
        routingTable.forwardMessage(message, propagator)
    }

    fun sendMessage(message: Message, destination: Node) {
        messageBroker.putMessage(message.to(destination))
    }

    override fun equals(other: Any?): Boolean {
        return user.username == (other as Peer).user.username
    }

    override fun hashCode(): Int {
        return user.username.hashCode()
    }

    fun addNeighbour(username: String, address: InetAddress, port: Int) {
        routingTable.addNeighbour(username, address, port)
    }

    companion object {
        fun freePort(): Int {
            val socket = ServerSocket(0)

            socket.reuseAddress = true

            val port = socket.localPort

            socket.close()

            return port
        }
    }
}