package gnutella.peer

import social.User
import java.io.Serializable
import java.net.InetAddress

abstract class Node(
    val user: User,
    val address: InetAddress = InetAddress.getByName("127.0.0.1"),
    val port: Int,
) : Serializable {
    override fun toString(): String {
        return "Peer $user"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true

        if (javaClass != other?.javaClass)
            return false

        other as Node

        return user == other.user
    }

    override fun hashCode(): Int {
        return user.username.hashCode()
    }
}