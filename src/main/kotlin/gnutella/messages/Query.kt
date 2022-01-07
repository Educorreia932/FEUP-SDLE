package gnutella.messages

import gnutella.peer.Node
import java.util.*

class Query(
    ID: UUID,
    var propagatorId: String,
    var propagatorAddress: String,
    var propagatorPort: Int,
    var timeToLive: Int,
    var hops: Int,
    val keyword: String,
) : Message(ID) {
    override fun cloneThis(): Message {
        return Query(ID, propagatorId, propagatorAddress, propagatorPort, timeToLive, hops, keyword)
    }
}