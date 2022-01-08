package gnutella.messages

import gnutella.peer.Node
import java.io.Serializable
import java.util.*

abstract class Message(
    val ID: UUID,
    val source: Node,
) : Serializable, Cloneable {
    var destination: Node? = null

    fun to(destination: Node): Message {
        val message = cloneThis()

        message.destination = destination

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
        return ID.hashCode()
    }
}