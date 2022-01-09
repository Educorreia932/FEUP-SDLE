package gnutella.handlers

import gnutella.messages.Pong
import gnutella.peer.Peer

class PongHandler(
    private val peer: Peer,
    private val message: Pong,
) : MessageHandler(message) {
    override fun run() {
        if (message.available)
            peer.addNeighbour(message.source as Peer, true)
    }
}