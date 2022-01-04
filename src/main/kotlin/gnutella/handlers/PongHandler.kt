package gnutella.handlers

import gnutella.messages.Message
import gnutella.peer.Neighbour
import gnutella.peer.Peer

class PongHandler(
    private val peer: Peer,
    private val message: Message,
    private val fromNeighbour: Neighbour,
) : MessageHandler(message) {
    override fun run() {
        peer.addNeighbour(message.destinationAddress!!, message.destinationPort!!)

        // TODO: Set timeout to remove neighbour
    }
}