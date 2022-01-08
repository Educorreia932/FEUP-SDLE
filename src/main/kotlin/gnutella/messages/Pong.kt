package gnutella.messages

import gnutella.peer.Node
import java.util.*

class Pong(
    ID: UUID,
    source: Node,
    private val address: String,
    val port: Int
) : Message(ID, source) {
    override fun cloneThis(): Message {
        return Pong(ID, source, address, port)
    }

    override fun toString(): String {
        return "Pong"
    }
}