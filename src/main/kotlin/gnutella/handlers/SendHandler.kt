package gnutella.handlers

import gnutella.messages.Send
import gnutella.peer.Peer

class SendHandler(
    private val peer: Peer,
    private val send: Send
) : MessageHandler(send) {
    override fun run() {
        println("Posts received: ${send.posts}")

        for (post in send.posts)
            peer.storage.addPost(post)
    }
}