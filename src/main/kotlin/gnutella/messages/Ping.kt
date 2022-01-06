package gnutella.messages

import gnutella.peer.Node
import java.util.*

class Ping(
    ID: UUID,
    var timeToLive: Int,
    var hops: Int,
) : Message(ID) {
    override fun cloneThis(): Message {
        return Ping(ID, timeToLive, hops)
    }
}