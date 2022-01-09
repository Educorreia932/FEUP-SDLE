package gnutella.handlers

import User
import gnutella.messages.Get
import gnutella.messages.QueryHit
import gnutella.peer.Peer
import java.util.*

class QueryHitHandler(
    private val peer: Peer,
    private val queryHit: QueryHit,
) : MessageHandler(queryHit) {
    override fun run() {
        val query = peer.cache.getCorrespondingQueryOrNull(queryHit)

        if (query != null) {
            val user = User(query.keyword)

            // We're following the user
            if (peer.user.isFollowing(user)) {
                // Select only the wanted posts (by removing the ones we already have)
                val wanted = queryHit.digest - peer.storage.digest(user)

                // There are wanted posts
                if (wanted.postIDs.isNotEmpty()) {
                    val get = Get(UUID.randomUUID(), peer, wanted)

                    peer.sendMessageTo(get, queryHit.source)
                }
            }

            if (peer.sentQueryIDs.contains(queryHit.ID))
                println("Peer ${peer.user.username} | Received a QueryHit response to it's previous query (MsgID = $queryHit.ID)")

            println("Peer ${peer.user.username} | Received known queryHit")
            println("${query.propagator.address} ${query.propagator.port}")

            peer.sendMessageTo(QueryHit(query.ID, queryHit.source, queryHit.digest), query.propagator)
        }
        
        else {
            println("Peer ${peer.user.username} | Received unknown $queryHit")
        }
    }
}