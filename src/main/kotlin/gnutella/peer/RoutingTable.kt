package gnutella.peer

import User
import gnutella.Constants
import gnutella.messages.Message
import gnutella.messages.Query
import org.graphstream.graph.Graph
import java.net.InetAddress

class RoutingTable(
    val peer: Peer, private val graph: Graph
) {
    val neighbours = mutableSetOf<Neighbour>()
    private val friends = mutableMapOf<String, Pair<Set<Neighbour>, Int>>()

    fun addNeighbour(username: String, address: InetAddress, port: Int) {
        addNeighbour(Neighbour(User(username), address, port))
    }

    fun addNeighbour(peer: Peer) {
        addNeighbour(Neighbour(peer))
    }

    fun addNeighbour(neighbour: Neighbour) {
        if (neighbour.user.username != peer.user.username && neighbour !in neighbours) {
            //TODO: Deve não se adicionar?
            if (neighbours.size == Constants.maxNeighbours) {
                return
            }

            neighbours.add(neighbour)

            graph.addEdge(
                "${peer.port}-${neighbour.port}", peer.port.toString(), neighbour.port.toString(), true
            )
        }
    }

    fun forwardMessage(message: Message, nodes: Set<Neighbour> = neighbours) {
        for (node in nodes) peer.sendMessage(message, node)
    }

    fun forwardMessage(query: Query) {
        // Forward message to friends lists, if there is one for the given keyword
        if (query.keyword in friends.keys) forwardMessage(query, friends[query.keyword]!!.first)
        else forwardMessage(query, neighbours)
    }

    fun forwardMessage(message: Message, propagator: Node) {
        for (neighbour in neighbours) if (neighbour != propagator) peer.sendMessage(message, neighbour)
    }
}