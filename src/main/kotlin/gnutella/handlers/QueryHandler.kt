package gnutella.handlers

import gnutella.messages.Query
import gnutella.messages.QueryHit
import gnutella.peer.Peer

class QueryHandler(
    private val peer: Peer,
    private val query: Query,
) : MessageHandler(query) {
    override fun run() {
        // Error check 
        // TODO: This may go to a super class method
        if (query.timeToLive == 0) {
            println("No time to live and/or num hops left in this message. Not propagating.")

            return
        }

        // Duplicate query received. Ignore.
        if (peer.cache.containsQuery(query)) {
            println("Peer ${peer.user.username} | Received duplicate query.")

            return
        }

        // Add to cache
        peer.cache.addQuery(query)

        // Send query hit back if node had the desired data
        val posts = peer.storage.retrievePosts(query.keyword)

        if (posts.isNotEmpty()) {
            val response = QueryHit(query.ID, peer, posts)

            peer.sendMessage(response, query.source)
        }

        // Increment hops and decrement time to live
        query.hops++
        query.timeToLive--

        // Don't propagate if it's reached the hop limit
        if (query.timeToLive <= 0) {
            println("Not propagating. Reached TTL=0.")

            return
        }

        println("Peer ${peer.user.username} propagating")

        // We're the propagator now
        val prevPropagator = query.propagatorId
        query.propagatorId = peer.user.username

        peer.forwardMessage(query, prevPropagator)
    }
}