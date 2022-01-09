package gnutella.handlers

import gnutella.messages.RemoveNeighbour
import gnutella.peer.Neighbour
import gnutella.peer.Peer

class RemoveNeighbourHandler(
    private val peer: Peer,
    private val removeNeighbour: RemoveNeighbour
) : MessageHandler(removeNeighbour) {
    override fun run() {
        peer.removeNeighbour(Neighbour(removeNeighbour.source))
    }
}