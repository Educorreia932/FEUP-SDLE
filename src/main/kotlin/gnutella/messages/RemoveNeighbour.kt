package gnutella.messages

import gnutella.peer.Node
import java.util.*

class RemoveNeighbour(
    ID: UUID,
    source: Node,
) : Message(ID, source) {
    override fun cloneThis(): Message {
        return RemoveNeighbour(ID, source)
    }

    override fun toString(): String {
        return "RemoveNeighbour"
    }
}