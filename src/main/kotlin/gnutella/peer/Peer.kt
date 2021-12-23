package gnutella.peer

import gnutella.handlers.DataHandler
import gnutella.messages.Message

/**
 * Representation of a Gnutella node
 */
class Peer(
    private val username: String,
    private val address: String = "224.0.0.15",
    private val port: Int,
) {
    private var neighbours = mutableListOf<Peer>()
    
    private val messageBroker = MessageBroker(address, port)
    private val dataHandler = DataHandler()

    fun connect(peer: Peer) {
        if (this != peer && peer !in neighbours) {
            neighbours.add(peer)
            peer.connect(this)
        }
    }

    fun search(keyword: String) {
        val message = Message(address, 8003, keyword)

        messageBroker.putMessage(message)
    }

    override fun equals(other: Any?): Boolean {
        return username == (other as Peer).username
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + neighbours.hashCode()

        return result
    }
}