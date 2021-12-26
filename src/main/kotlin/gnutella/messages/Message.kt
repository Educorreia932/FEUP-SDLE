package gnutella.messages

import gnutella.peer.Peer

// TODO: Implement as Serializable
abstract class Message {
    var destinationAddress: String? = null
    var destinationPort: Int? = null

    fun to(peer: Peer): Message {
        return to(peer.address, peer.port)
    }

    fun to(address: String, port: Int): Message {
        destinationAddress = address
        destinationPort = port

        return this
    }

    open fun toBytes(): ByteArray {
        return "${toString()}|${destinationAddress}|${destinationPort}".toByteArray()
    }

    companion object {
        fun fromBytes(bytes: ByteArray): Message? {
            val fields = String(bytes).split("|")
            println(fields)

            return when (fields[0]) {
                "PING" -> Ping(fields[1], fields[2].toInt())
                "PONG" -> Pong(fields[1], fields[2].toInt())
                "QUERY" -> Query(fields[1], fields[2].toInt(), fields[3])
                "QUERY_HIT" -> QueryHit()

                else -> {
                    null
                }
            }
        }
    }
}