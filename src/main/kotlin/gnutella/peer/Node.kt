package gnutella.peer

import User

abstract class Node(
    val user: User,
    val address: String = "127.0.0.1",
    val port: Int,
) 