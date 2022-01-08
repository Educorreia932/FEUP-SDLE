package gnutella.handlers

import gnutella.messages.QueryHit
import gnutella.peer.Peer

class QueryHitHandler(
    private val peer: Peer,
    val queryHit: QueryHit,
) : MessageHandler(queryHit) {
    override fun run() {
        // TODO: Add sender to list of friends of the given category
        
        println(queryHit.digest)
    }
}