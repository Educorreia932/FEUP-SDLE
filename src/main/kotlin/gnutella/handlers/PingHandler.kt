package gnutella.handlers

import gnutella.messages.Ping
import gnutella.messages.Pong
import gnutella.peer.Peer
import java.util.*

class PingHandler(
    private val peer: Peer,
    private var ping: Ping,
) : MessageHandler(ping) {
    override fun run() {

        if (peer.hasNoNeighbours()) peer.addNeighbour(ping.source as Peer, true)

        // Duplicate ping received. Ignore.
        if (ping in peer.cache) return

        ping = ping.cloneThis() as Ping
        peer.cache.addPing(ping)

        val response = Pong(UUID.randomUUID(), peer, !peer.hasMaxNeighbours())
        peer.sendMessageTo(response, ping.source)

        // Increment hops and decrement time to live
        ping.hops++
        ping.timeToLive--

        if (ping.timeToLive == 0) {
            println("No time to live and/or num hops left in this message. Not propagating.")
            return
        }

        val previousPropagator = ping.propagator
        ping.propagator = peer

        // Forward ping to neighbours
        peer.forwardMessage(ping, previousPropagator)
    }
}