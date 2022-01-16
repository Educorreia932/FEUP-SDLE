package gnutella.messages

import gnutella.peer.Node
import java.util.*

class Discover(
    ID: UUID,
    source: Node,
    var propagator: Node,
    val keyword: String,
    var timeToLive: Int,
    var hops: Int,
) : Message(ID, source) {
    override fun cloneThis(): Discover {
        return Discover(ID, source, propagator, keyword, timeToLive, hops)
    }

    override fun toString(): String {
        return "Discover"
    }
}