package gnutella.messages

import gnutella.Constants
import gnutella.peer.Node
import java.util.*

class Query(
    ID: UUID,
    source: Node,
    var propagator: Node,
    val keyword: String,
    var timeToLive: Int = Constants.TTL,
    var hops: Int = Constants.MAX_HOPS,
) : Message(ID, source) {
    override fun cloneThis(): Query {
        return Query(ID, source, propagator, keyword, timeToLive, hops)
    }

    override fun toString(): String {
        return "Query"
    }
}