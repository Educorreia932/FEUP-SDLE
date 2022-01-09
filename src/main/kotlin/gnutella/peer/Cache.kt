package gnutella.peer

import gnutella.messages.Message
import gnutella.messages.Ping
import gnutella.messages.Query

class Cache(
    val peer: Peer
) {
    val previousPingList: MutableList<Ping> = mutableListOf()
    private val previousQueryList: MutableList<Query> = mutableListOf()

    fun containsPing(ping: Ping): Boolean {
        return ping in previousPingList
    }

    fun containsQuery(query: Query): Boolean {
        return query in previousQueryList
    }

    //For queryHit
    fun containsQuery(queryHit: Message): Boolean {
        for (q in previousQueryList) {
            if (q.ID == queryHit.ID) {
                return true
            }
        }
        return false
    }

    //For queryHit
    fun getCorrespondingQueryOrNull(queryHit: Message): Query? {
        for (q in previousQueryList) {
            if (q.ID == queryHit.ID) {
                return q
            }
        }
        return null
    }

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