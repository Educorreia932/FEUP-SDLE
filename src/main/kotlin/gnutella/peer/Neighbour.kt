package gnutella.peer

import User
import java.net.InetAddress
import java.util.*

/**
 * Representation of a Gnutella node
 */
class Neighbour(
    user: User,
    address: InetAddress,
    port: Int,
) : Node(user, address, port) {
    val seenIDs: Set<UUID> = mutableSetOf()

    constructor(node: Node) : this(node.user, node.address, node.port)
}