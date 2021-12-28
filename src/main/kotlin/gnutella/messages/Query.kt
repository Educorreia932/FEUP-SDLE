package gnutella.messages

class Query constructor(
    val sourceAddress: String,
    val sourcePort: Int,
    var timeToLive: Int,
    var hops: Int,
    val keyword: String,
) : Message() {

    override fun cloneThis(): Message {
        return Query(sourceAddress, sourcePort, timeToLive, hops, keyword)
    }

    override fun toBytes(): ByteArray {
        return "${toString()}|${sourceAddress}|${sourcePort}|${timeToLive}|${hops}|${keyword}".toByteArray()
    }

    override fun toString(): String {
        return "QUERY"
    }
}