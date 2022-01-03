package gnutella.peer

import gnutella.messages.Ping
import gnutella.messages.Query

class Cache(
    val peer: Peer
) {
    private val previousPingList: MutableList<Ping> = mutableListOf<Ping>()
    private val previousQueryList: MutableList<Query> = mutableListOf<Query>()

    fun containsPing(ping: Ping): Boolean {
        for (p in previousPingList) {
            if (ping.isDuplicateOf(p))
                return true
        }
        return false
    }

    fun containsQuery(query: Query): Boolean {
        for (q in previousQueryList) {
            if (query.isDuplicateOf(q))
                return true
        }
        return false
    }

    fun addPing(ping: Ping) {
        previousPingList.add(ping)
    }

    fun addQuery(query: Query) {
        previousQueryList.add(query)
    }

}