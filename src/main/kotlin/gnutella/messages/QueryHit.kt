package gnutella.messages

import Digest
import gnutella.peer.Node
import java.util.*

class QueryHit(
    ID: UUID,
    source: Node,
    val digest: Digest
) : Message(ID, source) {
    override fun cloneThis(): Message {
        return QueryHit(ID, source, digest)
    }

    override fun toString(): String {
        return "QueryHit"
    }
}