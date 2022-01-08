package gnutella.messages

import gnutella.peer.Node
import java.util.*

class Pong(
    ID: UUID,
    source: Node,
) : Message(ID, source) {
    override fun cloneThis(): Message {
        return Pong(ID, source)
    }

    override fun toString(): String {
        return "Pong"
    }
}