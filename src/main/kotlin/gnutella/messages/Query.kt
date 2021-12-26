package gnutella.messages

class Query(
    val sourceAddress: String,
    val sourcePort: Int,
    val keyword: String,
) : Message() {
    override fun toBytes(): ByteArray {
        return "${toString()}|${sourceAddress}|${sourcePort}|$keyword".toByteArray()
    }

    override fun toString(): String {
        return "QUERY"
    }
}