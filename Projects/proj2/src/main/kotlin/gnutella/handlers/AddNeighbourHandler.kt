package gnutella.handlers

import gnutella.messages.AddNeighbour
import gnutella.messages.RemoveNeighbour
import gnutella.peer.Peer

class AddNeighbourHandler(
    private val peer: Peer, private val addNeighbour: AddNeighbour
) : MessageHandler(addNeighbour) {
    override fun run() {
        if (peer.hasMaxNeighbours()) {
            val neighbourToRemove = peer.removeRandomNeighbour()
            peer.addNeighbour(addNeighbour.source as Peer, false)
            peer.sendMessageTo(RemoveNeighbour(addNeighbour.ID, this.peer), neighbourToRemove)
        } else {
            peer.addNeighbour(addNeighbour.source as Peer, false)
        }
    }
}