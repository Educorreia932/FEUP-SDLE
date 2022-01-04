package gnutella.messages

import java.util.*

class Ping(
    var ID: UUID,
    var timeToLive: Int,
    var hops: Int,
) : Message(ID) {

    override fun cloneThis(): Message {
        return Ping(ID, timeToLive, hops)
    }

    override fun toString(): String {
        return "PING|${ID}"
    }

    //Checks if the ping is a duplicate of this ping. Duplicates don't need to have the same time to live or number of hops.
    fun isDuplicateOf(ping: Ping): Boolean {
        return ID == ping.ID
    }

    override fun toBytes(): ByteArray {
        return "PING|${ID}|${timeToLive}|${hops}|${destinationAddress}|${destinationPort}".toByteArray()
    }

    companion object {
        fun fromBytes(bytes: ByteArray): Ping {
            val fields = String(bytes).split("|")
            return Ping(UUID.fromString(fields[1]), fields[2].toInt(), fields[3].toInt())
        }
    }
}