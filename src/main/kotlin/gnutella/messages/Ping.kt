package gnutella.messages

class Ping(
    var sourceAddress: String,
    var sourcePort: Int,
    var timeToLive: Int,
    var hops: Int,
) : Message() {

    override fun cloneThis(): Message {
        return Ping(sourceAddress, sourcePort, timeToLive, hops)
    }
    override fun toString(): String {
        return "PING"
    }
}