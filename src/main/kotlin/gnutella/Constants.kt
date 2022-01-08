package gnutella

import java.net.InetAddress

class Constants {
    companion object {
        const val MAX_HOPS = 3  // Number of maximum hops
        const val TTL = 3000    // Time to live in miliseconds
        const val HOST_CACHE_PORT = 55555
        val HOST_CACHE_ADDRESS: InetAddress = InetAddress.getByName("127.0.0.1")
    }
}
