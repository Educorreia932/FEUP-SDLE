package gnutella.handlers

import gnutella.messages.Get
import gnutella.messages.Send
import gnutella.peer.Peer

class GetHandler(
    private val peer: Peer,
    private val get: Get
) : MessageHandler(get) {
    override fun run() {
        val posts = peer.user.storage.retrievePosts(get.digest)
        val send = Send(get.ID, peer, posts!!)

        peer.sendMessageTo(send, get.source)
    }
}