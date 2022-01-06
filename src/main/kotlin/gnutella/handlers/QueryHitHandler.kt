package gnutella.handlers

import gnutella.messages.Message
import gnutella.peer.Neighbour
import gnutella.peer.Peer

class QueryHitHandler(
    private val peer: Peer,
    private val message: Message,
) : MessageHandler(message) {
    override fun run() {

    }
}