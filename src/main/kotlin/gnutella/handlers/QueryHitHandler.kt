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
        // Destination address of queryHit isn't the target, it's the next one in line
        /*if(message.destinationAddress == peer.address && message.destinationPort == peer.port){
            println("Peer " + peer.user.username + " | " + "Got their query replied to by " + message)
            return
        }*/
        val query = peer.cache.getCorrespondingQueryOrNull(message)
        if(query != null){
            println("Peer " + peer.user.username + " | Received known queryHit")
            println(query.propagatorAddress + query.propagatorPort)
            peer.sendMessageTo(QueryHit(query.ID), query.propagatorAddress, query.propagatorPort)
            return
        }
        println("Peer " + peer.user.username + " | Received unknown queryHit")
    }
}