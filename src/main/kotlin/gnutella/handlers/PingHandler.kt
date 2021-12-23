package gnutella.handlers

import gnutella.messages.Message
import gnutella.peer.Peer

class PingHandler(
    private val peer: Peer,
    private val message: Message
) : MessageHandler(message), Runnable {
    override fun run() {
        // Forward ping to neighbours
        for (neighbour in peer.neighbours) {
            peer.messageBroker.putMessage(message)
        }
        
        
    }
}