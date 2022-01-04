package gnutella.messages

import gnutella.peer.Neighbour
import gnutella.peer.Peer
import java.io.Serializable
import java.util.*

abstract class Message(ID: UUID): Serializable {
    var destinationAddress: String? = null
    var destinationPort: Int? = null

    fun to(neighbour: Neighbour): Message {
        return to(neighbour.address, neighbour.port)
    }

    fun to(address: String, port: Int): Message {
        val msg = cloneThis()
        
        msg.destinationAddress = address
        msg.destinationPort = port
        
        return msg
    }

    open fun toBytes(): ByteArray {
        return "${toString()}|${destinationAddress}|${destinationPort}".toByteArray()
    }

    companion object {
        fun fromBytes(bytes: ByteArray): Message? {
            val fields = String(bytes).split("|")
            println(fields)

            return when (fields[0]) {
                "PING" -> Ping.fromBytes(bytes)
                "PONG" -> Pong.fromBytes(bytes)
                "QUERY" -> Query.fromBytes(bytes)
                "QUERY_HIT" -> QueryHit.fromBytes(bytes)

                else -> {
                    null
                }
            }
        }
    }

    abstract fun cloneThis(): Message
}