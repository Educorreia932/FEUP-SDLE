package gnutella.messages

class Query constructor(
    val sourceAddress: String,
    val sourcePort: Int,
    var timeToLive: Int,
    var hops: Int,
    val keyword: String,
) : Message() {

    override fun to(address: String, port: Int): Message {
        val query = Query(address, port, timeToLive, hops, keyword)

        query.destinationPort = port
        query.destinationAddress = address
        return query
    }

    override fun toBytes(): ByteArray {
        return "${toString()}|${sourceAddress}|${sourcePort}|${keyword}|${timeToLive}|${hops}".toByteArray()
    }

    override fun toString(): String {
        return "QUERY"
    }
}