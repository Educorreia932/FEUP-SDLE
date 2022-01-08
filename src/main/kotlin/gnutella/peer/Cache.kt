package gnutella.peer

import gnutella.messages.Ping
import gnutella.messages.Query

class Cache(
    val peer: Peer
) {
    private val previousPingList: MutableList<Ping> = mutableListOf()
    private val previousQueryList: MutableList<Query> = mutableListOf()

    fun addPing(ping: Ping) {
        previousPingList.add(ping)
    }

    fun addQuery(query: Query) {
        previousQueryList.add(query)
    }

    operator fun contains(ping: Ping): Boolean {
        return ping in previousPingList
    }

    operator fun contains(query: Query): Boolean {
        return query in previousQueryList
    }
}