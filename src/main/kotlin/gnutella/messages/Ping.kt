package gnutella.messages

import gnutella.peer.Peer

class Ping(
    sourceAddress: String,
    sourcePort: Int,
    var timeToLive: Int,
    var hops: Int,
) : Message() {

    override fun to(address: String, port: Int): Message {
        val ping = Ping(address, port, timeToLive, hops)
        ping.destinationPort = port
        ping.destinationAddress = address
        return ping
    }

    override fun toString(): String {
        return "PING"
    }
}