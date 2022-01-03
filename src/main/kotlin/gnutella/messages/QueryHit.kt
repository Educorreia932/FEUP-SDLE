package gnutella.messages

class QueryHit : Message() {

    override fun cloneThis(): Message {
        return QueryHit()
    }

    override fun toString(): String {
        return "QUERY_HIT"
    }
}