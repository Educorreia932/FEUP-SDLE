package gnutella.messages

import java.util.*

class QueryHit(
    ID: UUID,
) : Message(ID) {
    override fun cloneThis(): Message {
        return QueryHit(ID)
    }
}