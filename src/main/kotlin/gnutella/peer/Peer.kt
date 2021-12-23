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
    val neighbours = mutableListOf<Peer>()
    val messageBroker = MessageBroker(this, address, port)
    private val dataHandler = DataHandler()

    fun addNeighbour(address: String, port: Int) {
        addNeighbour(Peer("", address, port))
    }

    fun addNeighbour(peer: Peer) {
        if (this != peer && peer !in neighbours) {
            neighbours.add(peer)
            peer.addNeighbour(this)
        }
    }

    fun ping() {
        val data = "PING"
        val message = Message("127.0.0.1", 8002, data)

        messageBroker.putMessage(message)
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