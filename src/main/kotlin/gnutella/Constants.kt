package gnutella

import java.net.InetAddress

class Constants {
    companion object {
        const val TTL = 3    // Maximum hops of message
        const val HOST_CACHE_PORT = 55555
        val HOST_CACHE_ADDRESS: InetAddress = InetAddress.getByName("127.0.0.1")
        const val maxNeighbours = 3
        const val MAX_FRIENDS_TO_MESSAGE = 5 // Send querries to (up to) this many friends
    }
}
