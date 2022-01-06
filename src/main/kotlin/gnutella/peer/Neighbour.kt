package gnutella.peer

import User
import java.util.*

/**
 * Representation of a Gnutella node
 */
class Neighbour(
    user: User,
    address: String = "127.0.0.1",
    port: Int,
) : Node(user, address, port) {
    val seenIDs: Set<UUID> = mutableSetOf()

    constructor(peer: Peer) : this(peer.user, peer.address, peer.port)

    override fun hashCode(): Int {
        return user.username.hashCode()
    }

    fun sameAsPeer(peer: Peer): Boolean {
        return user.username == peer.user.username
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true

        if (javaClass != other?.javaClass)
            return false

        other as Neighbour

        return user.username == other.user.username
    }

}