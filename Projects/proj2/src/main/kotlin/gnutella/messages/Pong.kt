package gnutella.messages

import gnutella.peer.Node
import java.util.*

class Pong(
    ID: UUID,
    source: Node,
    val available: Boolean
) : Message(ID, source) {
    override fun cloneThis(): Message {
        return Pong(ID, source, available)
    }

    override fun toString(): String {
        return "Pong"
    }
}