package gnutella.handlers

import gnutella.messages.Message
import gnutella.peer.Neighbour
import gnutella.peer.Peer

class PongHandler(
    private val peer: Peer,
    private val message: Message,
) : MessageHandler(message) {
    override fun run() {
        peer.addNeighbour(message.source.user.username, message.source.address, message.source.port)
    }
}