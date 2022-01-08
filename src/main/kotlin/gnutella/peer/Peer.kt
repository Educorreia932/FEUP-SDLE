package gnutella.peer

import User
import gnutella.Constants
import gnutella.messages.Message
import gnutella.messages.Ping
import gnutella.messages.Query
import org.graphstream.graph.Graph
import java.net.InetAddress
import java.net.ServerSocket
import java.util.*

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
    val neighbours = mutableSetOf<Neighbour>()

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
        addNeighbour(peer)

        ping()
    }

    fun addNeighbour(username: String, address: InetAddress, port: Int) {
        addNeighbour(Neighbour(User(username), address, port))
    }

    fun addNeighbour(peer: Peer) {
        addNeighbour(Neighbour(peer))
    }

    private fun addNeighbour(neighbour: Neighbour) {
        if (neighbour.user.username != user.username && neighbour !in neighbours) {
            neighbours.add(neighbour)

            graph.addEdge(
                "${port}-${neighbour.port}",
                port.toString(),
                neighbour.port.toString(),
                true
            )
        }
    }

    private fun ping() {
        val message = Ping(UUID.randomUUID(), this, user.username, 3000, Constants.MAX_HOPS)

        forwardMessage(message)
    }

    fun search(keyword: String) {
        val message = Query(UUID.randomUUID(), this, user.username, Constants.MAX_HOPS, 0, keyword)

        forwardMessage(message)
    }

    fun sendMessage(message: Message, destination: Node) {
        messageBroker.putMessage(message.to(destination))
    }

    private fun forwardMessage(message: Message) {
        for (neighbour in neighbours)
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