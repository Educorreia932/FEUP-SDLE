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

    //Checks if the ping is a duplicate of this ping. Duplicates don't need to have the same time to live or number of hops.
    fun isDuplicateOf(query: Query): Boolean{
        return sourceAddress.equals(query.sourceAddress) && sourcePort == query.sourcePort && destinationAddress.equals(query.destinationAddress) && destinationPort == query.destinationPort
    }

}