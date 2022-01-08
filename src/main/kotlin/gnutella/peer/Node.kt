package gnutella.peer

import User
import java.io.Serializable
import java.net.InetAddress

abstract class Node(
    val user: User,
    val address: InetAddress = InetAddress.getByName("127.0.0.1"),
    val port: Int,
) : Serializable