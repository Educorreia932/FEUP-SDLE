package gnutella.messages


import gnutella.peer.Node
import java.util.*

class RequestConnect(
    ID: UUID,
    source: Node,
) : Message(ID, source) {
    override fun cloneThis(): Message {
        return RequestConnect(ID, source)
    }

    override fun toString(): String {
        return "RequestConnect"
    }
}
