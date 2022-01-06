package gnutella.peer

import gnutella.messages.Ping
import gnutella.messages.Query

class Cache(
    val peer: Peer
) {
    private val previousPingList: MutableList<Ping> = mutableListOf()
    private val previousQueryList: MutableList<Query> = mutableListOf()

    fun containsPing(ping: Ping): Boolean {
        return ping in previousPingList
    }

    fun containsQuery(query: Query): Boolean {
        return query in previousQueryList
    }

    fun addPing(ping: Ping) {
        previousPingList.add(ping)
    }

    fun addQuery(query: Query) {
        previousQueryList.add(query)
    }

}