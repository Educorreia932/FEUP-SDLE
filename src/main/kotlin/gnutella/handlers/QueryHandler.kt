package gnutella.handlers

import gnutella.messages.Query
import gnutella.messages.QueryHit
import gnutella.peer.Peer

class QueryHandler(
    private val peer: Peer,
    private val query: Query
) : MessageHandler(query){
    override fun run() {
        val posts = peer.storage.retrievePosts(query.keyword)

        if (posts.isNotEmpty()) {
            val response = QueryHit()

            peer.sendMessage(response, query.sourceAddress, query.sourcePort)
        }

        if(query.timeToLive == null || query.hops == null){
            println("No time to live and/or num hops left in this message. Not propagating.")
            return;
        }

        //Increment hops and decrement time to live
        query.hops = query.hops!! - 1;
        query.timeToLive = query.timeToLive!! - 1;

        println("Peer: ${peer.user.username} ; TTL: ${query.timeToLive}")
        //Don't propagate if it's reached the hop limit
        if(query.timeToLive!! <= 0){
            println("Not propagating. Reached TTL=0.")
            return;
        }
        
        peer.forwardMessage(query)
    }
}