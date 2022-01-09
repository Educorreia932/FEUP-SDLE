package gnutella.peer

import gnutella.Constants
import gnutella.messages.AddNeighbour
import gnutella.messages.Message
import gnutella.messages.Query
import org.graphstream.graph.Graph
import java.lang.Integer.min
import java.util.*

class RoutingTable(
    val peer: Peer, private val graph: Graph
) {
    val neighbours = mutableSetOf<Neighbour>()
    val friends =
        FriendList()//mutableMapOf<String, Map<Neighbour, Float>>() // Topic -> (Friends -> Relationship score)


    fun addNeighbour(peer: Peer, notify: Boolean) {
        addNeighbour(Neighbour(peer), notify)
    }

    fun addNeighbour(neighbour: Neighbour, notify: Boolean) {
        if (neighbours.size == Constants.maxNeighbours) {
            return
        }

        if (neighbour.user.username != peer.user.username && neighbour !in neighbours) {
            val added = neighbours.add(neighbour)

            if (added && notify)
                peer.sendMessageTo(AddNeighbour(UUID.randomUUID(), this.peer), neighbour)

            graph.addEdge(
                "${peer.port}-${neighbour.port}", peer.port.toString(), neighbour.port.toString(), true
            )
        }
    }

    fun removeNeighbour(neighbour: Neighbour) {
        neighbours.remove(neighbour)
        try {
            graph.removeEdge(
                "${peer.port}-${neighbour.port}"
            )
        } catch (_: Exception) {
        }
        if (neighbours.size == 0) {
            peer.connect()
        }
    }

    fun forwardMessage(message: Message, nodes: Set<Neighbour> = neighbours) {
        for (node in nodes) peer.sendMessageTo(message, node)
    }

    fun forwardMessageN(message: Message, nodes: Set<Neighbour> = neighbours, max_targets: Int) {
        var i = 0
        for (node in nodes){
            peer.sendMessageTo(message, node)
            i++
            if(i >= max_targets)
                break
        }
    }

    fun forwardMessage(query: Query) {
        val friends = friends.getBestFriendsForTopic(Constants.MAX_FRIENDS_TO_MESSAGE, query.keyword)
        // Forward message to friends lists, if there is one for the given keyword
        if (friends.isNotEmpty()){
            forwardMessage(query, friends.toSet())
            if(friends.size < Constants.MAX_FRIENDS_TO_MESSAGE){
                forwardMessageN(query, neighbours, Constants.MAX_FRIENDS_TO_MESSAGE - friends.size)
            }
        }
        else forwardMessage(query, neighbours)
    }

    fun forwardMessage(query: Query, propagator: Node) {
        val friends = friends.getBestFriendsForTopicExcept(Constants.MAX_FRIENDS_TO_MESSAGE, query.keyword, propagator)
        // Forward message to friends lists, if there is one for the given keyword
        if (friends.isNotEmpty()) forwardMessage(query, friends.toSet())
        else forwardMessage(query, neighbours)
    }

    fun forwardMessage(message: Message, propagator: Node) {
        for (neighbour in neighbours) if (neighbour != propagator) peer.sendMessageTo(message, neighbour)
    }
}