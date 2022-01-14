package gnutella.handlers

import gnutella.messages.Send
import gnutella.peer.Peer

class SendHandler(
    private val peer: Peer,
    private val send: Send
) : MessageHandler(send) {
    override fun run() {

        if(send.isSearch){
            peer.storage.addSearchPosts(send.posts)
        }
        else
            for (post in send.posts)
                peer.storage.addPost(post)
    }
}