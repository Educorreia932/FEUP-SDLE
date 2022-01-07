package gnutella.handlers

import gnutella.messages.Message
import gnutella.messages.Query
import gnutella.messages.QueryHit
import gnutella.peer.Neighbour
import gnutella.peer.Peer

class QueryHitHandler(
    private val peer: Peer,
    private val message: QueryHit,
) : MessageHandler(message) {
    override fun run() {
        if(message.destinationAddress == peer.address && message.destinationPort == peer.port){
            println("Peer " + peer.user.username + " | " + "Received a queryhit for message of ID = " + message.ID)
            return
        }
        val query = peer.cache.getCorrespondingQueryOrNull(message)
        if(query != null){
            println("Peer " + peer.user.username + " | Received known queryHit")
            peer.sendMessageTo(QueryHit(query.ID), query.propagatorAddress, query.propagatorPort)
            return
        }
        println("Peer " + peer.user.username + " | Received unknown queryHit")
    }
}