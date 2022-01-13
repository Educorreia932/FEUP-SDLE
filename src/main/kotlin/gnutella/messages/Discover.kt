package gnutella.messages

import gnutella.peer.Node
import java.util.*

class Discover(
    ID: UUID,
    source: Node,
    var propagator: Node,
    val keywordString: String,
    var timeToLive: Int,
    var hops: Int,
) : Message(ID, source) {
    override fun cloneThis(): Message {
        return Discover(ID, source, propagator, keywordString, timeToLive, hops)
    }
}