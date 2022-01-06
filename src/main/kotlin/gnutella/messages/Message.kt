package gnutella.messages

import gnutella.peer.Neighbour
import java.io.Serializable
import java.util.*

abstract class Message(
    val ID: UUID
) : Serializable, Cloneable {
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

    abstract fun cloneThis(): Message

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true

        if (javaClass != other?.javaClass)
            return false

        other as Message

        if (ID != other.ID)
            return false

        return true
    }

    override fun hashCode(): Int {
        var result = ID.hashCode()
        
        result = 31 * result + (destinationAddress?.hashCode() ?: 0)
        result = 31 * result + (destinationPort ?: 0)
        
        return result
    }
}