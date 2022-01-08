package gnutella.messages

import gnutella.peer.Node
import java.util.*

class Ping(
    ID: UUID,
    source: Node,
    var propagator: Node,
    var timeToLive: Int,
    var hops: Int,
) : Message(ID, source) {
    override fun cloneThis(): Message {
        return Ping(ID, source, propagator, timeToLive, hops)
    }

    override fun toString(): String {
        return "Ping"
    }
}