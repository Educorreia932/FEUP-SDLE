package gnutella.handlers

import gnutella.messages.QueryHit
import gnutella.peer.Peer

class QueryHitHandler(
    private val peer: Peer,
    val message: QueryHit,
) : MessageHandler(message) {
    override fun run() {
        for (post in message.posts)
            println(post.content)
    }
}