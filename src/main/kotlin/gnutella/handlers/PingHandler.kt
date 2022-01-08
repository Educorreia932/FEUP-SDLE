package gnutella.handlers

import gnutella.messages.Ping
import gnutella.messages.Pong
import gnutella.peer.Peer
import java.util.*

class PingHandler(
    private val peer: Peer,
    private val ping: Ping,
) : MessageHandler(ping) {
    override fun run() {
        if (ping.timeToLive == 0 || ping.hops == 0) {
            println("No time to live and/or num hops left in this message. Not propagating.")

            return
        }

        val response = Pong(UUID.randomUUID(), peer)

        peer.sendMessage(response, ping.source)

        // Duplicate ping received. Ignore.
        if (ping in peer.cache)
            return

        peer.cache.addPing(ping)

        // Increment hops and decrement time to live
        ping.hops++
        ping.timeToLive--

        // We're the propagator now
        val previousPropagator = ping.propagator
        ping.propagator = peer

        // Forward ping to neighbours
        peer.forwardMessage(ping, previousPropagator)
    }
}