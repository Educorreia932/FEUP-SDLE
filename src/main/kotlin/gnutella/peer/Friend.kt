package gnutella.peer

import User
import java.net.InetAddress

class Friend(
    user: User,
    address: InetAddress,
    port: Int
) : Neighbour(user, address, port) {
    var score: Int = 1

    companion object {
        @JvmStatic
        fun create(node: Node): Friend {
            return Friend(node.user, node.address, node.port)
        }
    }
}