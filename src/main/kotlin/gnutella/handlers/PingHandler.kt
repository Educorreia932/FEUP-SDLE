package gnutella.handlers

import gnutella.messages.Message
import gnutella.peer.Peer

class PingHandler(
    private val peer: Peer,
    private val message: Message
) : MessageHandler(message) {
    override fun run() {
        // Forward ping to neighbours
        peer.forwardMessage(message)
        
    }
}