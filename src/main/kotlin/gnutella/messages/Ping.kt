package gnutella.messages

import gnutella.peer.Node
import java.util.*

class Ping(
    ID: UUID,
    var propagatorId: String,
    var timeToLive: Int,
    var hops: Int,
) : Message(ID) {
    override fun cloneThis(): Message {
        return Ping(ID, propagatorId, timeToLive, hops)
    }
}