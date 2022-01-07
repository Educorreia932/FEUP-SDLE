package gnutella.messages

import gnutella.peer.Node
import java.util.*

class Ping(
    ID: UUID,
    var propagatorId: String,
    var propagatorAddress: String,
    var propagatorPort: Int,
    var timeToLive: Int,
    var hops: Int,
) : Message(ID) {
    override fun cloneThis(): Message {
        return Ping(ID, propagatorId, propagatorAddress, propagatorPort, timeToLive, hops)
    }
}