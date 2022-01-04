package gnutella.messages

import java.util.*

class QueryHit constructor(
    val ID: UUID
) : Message(ID) {

    override fun cloneThis(): Message {
        return QueryHit(ID)
    }

    override fun toString(): String {
        return "QUERY_HIT"
    }

    override fun toBytes(): ByteArray {
        return "QUERY_HIT|${ID}".toByteArray()
    }

    companion object {
        fun fromBytes(bytes: ByteArray): QueryHit {
            val fields = String(bytes).split("|")
            return QueryHit(UUID.fromString(fields[1]))
        }
    }
}