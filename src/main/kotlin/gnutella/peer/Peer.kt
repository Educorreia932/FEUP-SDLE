package gnutella.peer

import gnutella.handlers.DataHandler

/**
 * Representation of a Gnutella node
 */
class Peer(
    private val username: String
) {
    private var neighbours = mutableListOf<Peer>()
    val address: String = "" 
    val port: Int = 0
    private val controller = PeerProcessor(address, port)
    private val dataHandler = DataHandler()

    fun connect(peer: Peer) {
        if (this != peer && peer !in neighbours) {
            neighbours.add(peer)
            peer.connect(this)
        }
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