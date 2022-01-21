package gnutella.messages

import gnutella.peer.Node
import java.util.*

class AddNeighbour(
    ID: UUID,
    source: Node,
) : Message(ID, source) {
    override fun cloneThis(): Message {
        return AddNeighbour(ID, source)
    }

    override fun toString(): String {
        return "AddNeighbour"
    }
}