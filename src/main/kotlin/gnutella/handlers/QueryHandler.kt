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
        
        peer.forwardMessage(query)
    }
}