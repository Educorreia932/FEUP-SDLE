package gnutella.messages

import java.util.*

class Query constructor(
    val ID: UUID,
    var timeToLive: Int,
    var hops: Int,
    val keyword: String,
) : Message(ID) {

    override fun cloneThis(): Message {
        return Query(ID, timeToLive, hops, keyword)
    }

    override fun toBytes(): ByteArray {
        return "QUERY|${timeToLive}|${hops}|${keyword}".toByteArray()
    }

    companion object {
        fun fromBytes(bytes: ByteArray): Query {
            val fields = String(bytes).split("|")
            return Query(UUID.fromString(fields[1]), fields[2].toInt(), fields[3].toInt(), fields[4])
        }
    }

    //Checks if the ping is a duplicate of this ping. Duplicates don't need to have the same time to live or number of hops.
    fun isDuplicateOf(query: Query): Boolean {
        return ID == query.ID
    }

}