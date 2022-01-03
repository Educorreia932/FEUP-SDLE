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

    //Checks if the ping is a duplicate of this ping. Duplicates don't need to have the same time to live or number of hops.
    fun isDuplicateOf(ping: Ping): Boolean {
        return sourceAddress == ping.sourceAddress && sourcePort == ping.sourcePort && destinationAddress.equals(
            ping.destinationAddress
        ) && destinationPort == ping.destinationPort
    }
}