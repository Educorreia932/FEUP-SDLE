package gnutella.messages

import gnutella.peer.Node
import java.util.*

class ConnectTo(
    ID: UUID,
    source: Node,
    val possibleNeighbours: Set<Node>
) : Message(ID, source) {
    override fun cloneThis(): Message {
        return ConnectTo(ID, source, possibleNeighbours)
    }

    override fun toString(): String {
        return "ConnectTo"
    }
}
