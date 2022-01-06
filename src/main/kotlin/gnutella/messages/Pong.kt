package gnutella.messages

import gnutella.peer.Node
import java.util.*

class Pong(
    ID: UUID,
    val address: String,
    val port: Int
) : Message(ID) {
    override fun cloneThis(): Message {
        return Pong(ID, address, port)
    }
}