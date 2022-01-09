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
    val friends =
        FriendList()//mutableMapOf<String, Map<Neighbour, Float>>() // Topic -> (Friends -> Relationship score)

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

    fun removeNeighbour(neighbour: Neighbour) {
        neighbours.remove(neighbour)
    }

    fun forwardMessage(message: Message, nodes: Set<Neighbour> = neighbours) {
        for (node in nodes) peer.sendMessage(message, node)
    }

    fun forwardMessage(query: Query) {
        val friends = friends.getBestFriendsForTopic(Constants.MAX_FRIENDS_TO_MESSAGE, query.keyword)
        // Forward message to friends lists, if there is one for the given keyword
        if (friends.isNotEmpty())
            forwardMessage(query, friends.toSet())
        else
            forwardMessage(query, neighbours)
    }

    fun forwardMessage(query: Query, propagator: Node) {
        val friends = friends.getBestFriendsForTopicExcept(Constants.MAX_FRIENDS_TO_MESSAGE, query.keyword, propagator)
        // Forward message to friends lists, if there is one for the given keyword
        if (friends.isNotEmpty())
            forwardMessage(query, friends.toSet())
        else
            forwardMessage(query, neighbours)
    }

    fun forwardMessage(message: Message, propagator: Node) {
        for (neighbour in neighbours) if (neighbour != propagator) peer.sendMessage(message, neighbour)
    }
}