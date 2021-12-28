package gnutella.messages

class QueryHit: Message() {
    override fun to(address: String, port: Int): Message {
        val queryhit = QueryHit()
        queryhit.destinationPort = port
        queryhit.destinationAddress = address
        return queryhit
    }

    override fun toString(): String {
        return "QUERY_HIT"
    }
}