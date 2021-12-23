package gnutella.handlers

import gnutella.messages.Message
import gnutella.peer.Peer

class PongHandler(
    private val peer: Peer,
    private val message: Message
) : MessageHandler(message), Runnable {
    override fun run() {
        peer.addNeighbour(message.address, message.port)
    }
}