package gnutella.messages

import gnutella.peer.Node
import java.io.Serializable
import java.net.InetAddress
import java.util.*

abstract class Message(
    val ID: UUID,
    val source: Node
) : Serializable, Cloneable {
    // TODO: Use node instead
    var destinationAddress: InetAddress? = null
    var destinationPort: Int? = null

    fun to(neighbour: Node): Message {
        return to(neighbour.address, neighbour.port)
    }

    private fun to(address: InetAddress, port: Int): Message {
        val message = cloneThis()

        message.destinationAddress = address
        message.destinationPort = port

        return message
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