package gnutella.handlers

import gnutella.messages.Send
import gnutella.peer.Peer

class SendHandler(
    private val peer: Peer,
    private val send: Send
) : MessageHandler(send) {
    override fun run() {

        if(send.isSearch){
            peer.user.storage.addSearchPosts(send.posts)
        }
        else
            for (post in send.posts)
                peer.user.storage.addPost(post)
    }
}