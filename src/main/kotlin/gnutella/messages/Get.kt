package gnutella.messages

import Digest
import gnutella.peer.Node
import java.util.*

class Get(
    ID: UUID,
    source: Node,
    val digest: Digest
) : Message(ID, source) {
    override fun cloneThis(): Get {
        return Get(ID, source, digest)
    }
}