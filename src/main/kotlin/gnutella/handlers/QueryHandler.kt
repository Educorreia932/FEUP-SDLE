package gnutella.handlers

import User
import gnutella.messages.Query
import gnutella.messages.QueryHit
import gnutella.peer.Peer

class QueryHandler(
    private val peer: Peer,
    private var query: Query,
) : MessageHandler(query) {
    override fun run() {
        // Error check 
        if (query.timeToLive == 0) {
            println("No time to live and/or num hops left in this message. Not propagating.")

            return
        }

        // Duplicate query received. Ignore.
        if (query in peer.cache) {
            println("Peer ${peer.user.username} | Received duplicate query.")

            return
        }

        // Add to cache
        peer.cache.addQuery(query)

        // Send QueryHit back if node had the desired data
        val digest = peer.storage.digest(User(query.keyword)) - query.digest
        
        if (digest.postIDs.isNotEmpty()) {
            val response = QueryHit(query.ID, peer, digest)

            peer.sendMessage(response, query.source)
        }

        // Increment hops and decrement time to live
        query = query.cloneThis() as Query
        query.hops++
        query.timeToLive--

        // Don't propagate if it's reached the hop limit
        if (query.timeToLive <= 0) {
            println("Not propagating. Reached TTL=0.")

            return
        }

        println("Peer ${peer.user.username} propagating")

        // We're the propagator now
        val prevPropagator = query.propagator
        query.propagator = peer

        peer.forwardMessage(query, prevPropagator)
    }
}