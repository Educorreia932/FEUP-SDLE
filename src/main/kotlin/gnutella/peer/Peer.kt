package gnutella.peer

import Post
import User
import gnutella.Constants
import gnutella.messages.*
import org.graphstream.graph.Graph
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
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
    private val routingTable = RoutingTable(this, graph)

    @Transient
    private val messageBroker = MessageBroker(this)

    @Transient
    val cache = Cache(this)

    @Transient
    val storage = Storage()

    @Transient
    val sentQueryIDs = mutableSetOf<UUID>()

    init {
        val node = graph.addNode(port.toString())

        node.setAttribute("ui.label", "Peer ${user.username}")
    }

    fun connect() {
        val socket = Socket(Constants.HOST_CACHE_ADDRESS, Constants.HOST_CACHE_PORT)
        var possibleNeighbours: Set<Node>

        socket.use {
            val objectOutputStream = ObjectOutputStream(socket.getOutputStream())

            objectOutputStream.use {
                objectOutputStream.writeObject(RequestConnect(UUID.randomUUID(), this))

                var response: ConnectTo
                val objectInputStream = ObjectInputStream(socket.getInputStream())

                objectInputStream.use {
                    response = objectInputStream.readObject() as ConnectTo
                }

                possibleNeighbours = response.possibleNeighbours
            }
        }

        if (possibleNeighbours.isNotEmpty()) {
            for (possibleNeighbour in possibleNeighbours)
                routingTable.addNeighbour(Neighbour(possibleNeighbour as Peer))

            ping()
        }
    }

    private fun ping() {
        val message = Ping(UUID.randomUUID(), this, this, Constants.TTL, Constants.MAX_HOPS)

        cache.addPing(message)
        routingTable.forwardMessage(message)
    }

    fun search(username: String) {
        val message = Query(
            UUID.randomUUID(),
            this,
            this,
            username,
        )
        cache.addQuery(message)
        sentQueryIDs.add(message.ID)
        routingTable.forwardMessage(message)
    }

    fun forwardMessage(query: Query, propagator: Node) {
        routingTable.forwardMessage(query, propagator)
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

    fun sendMessageTo(message: Message, propagator: Node) {
        messageBroker.putMessage(message.to(propagator))
    }

    fun addNeighbour(peer: Peer) {
        routingTable.addNeighbour(peer)
    }

    fun removeNeighbour(neighbour: Neighbour) {
        routingTable.removeNeighbour(neighbour)
    }

    fun hasNoNeighbours(): Boolean {
        return routingTable.neighbours.isEmpty()
    }
    
    fun timeline(): List<Post> {
        return storage.timeline(user)
    }

    fun addFriendMessage(queryHit: QueryHit, me: Peer) {
        routingTable.friends.addFriendMessage(queryHit, queryHit.digest.user.username, me)
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