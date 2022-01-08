package gnutella.peer

import User
import gnutella.messages.Message
import org.graphstream.graph.Graph
import java.net.InetAddress

class RoutingTable(
    val peer: Peer,
    private val graph: Graph
) {
    private val neighbours = mutableSetOf<Neighbour>()

    fun addNeighbour(username: String, address: InetAddress, port: Int) {
        addNeighbour(Neighbour(User(username), address, port))
    }

    fun addNeighbour(peer: Peer) {
        addNeighbour(Neighbour(peer))
    }

    private fun addNeighbour(neighbour: Neighbour) {
        if (neighbour.user.username != peer.user.username && neighbour !in neighbours) {
            neighbours.add(neighbour)

            graph.addEdge(
                "${peer.port}-${neighbour.port}",
                peer.port.toString(),
                neighbour.port.toString(),
                true
            )
        }
    }

    fun forwardMessage(message: Message) {
        for (neighbour in neighbours)
            peer.sendMessage(message, neighbour)
    }

    fun forwardMessage(message: Message, propagator: Node) {
        for (neighbour in neighbours) {
            if (neighbour != propagator)
                peer.sendMessage(message, neighbour)
        }
    }
}