package gnutella.peer

import gnutella.messages.Message

/**
 * Representation of a Gnutella node
 */
class Peer(
    val username: String,
    private val address: String = "127.0.0.1",
    val port: Int,
) {
    val neighbours = mutableSetOf<Peer>()
    val messageBroker = MessageBroker(this)
    private val dataHandler = DataHandler()

    fun addNeighbour(address: String, port: Int) {
        addNeighbour(Peer("", address, port))
    }

    fun addNeighbour(peer: Peer) {
        if (this != peer && peer !in neighbours)
            neighbours.add(peer)
    }

    fun removeNeighbour(address: String, port: Int) {
        neighbours.remove(Peer("", address, port))
    }

    fun removeNeighbour(peer: Peer) {
        if (this != peer && peer in neighbours)
            neighbours.remove(peer)
    }

    fun ping() {
        val data = "PING"
        val message = Message(address, port, data)

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