package gnutella.messages

import java.util.*

data class Pong(
    val ID: UUID,
    val address: String,
    val port: Int
) : Message(ID) {

    override fun cloneThis(): Message {
        return Pong(ID, address, port)
    }

    override fun toString(): String {
        return "PONG"
    }

    override fun toBytes(): ByteArray {
        return "PONG|${ID}|${address}|${port}|${destinationAddress}|${destinationPort}".toByteArray()
    }

    companion object {
        fun fromBytes(bytes: ByteArray): Pong {
            val fields = String(bytes).split("|")
            return Pong(UUID.fromString(fields[1]), fields[2], fields[3].toInt())
        }
    }
}