package gnutella.messages

import Post
import gnutella.peer.Node
import java.util.*

class Send(
    ID: UUID,
    source: Node,
    val posts: List<Post>
) : Message(ID, source) {
    override fun cloneThis(): Message {
        return Send(ID, source, posts)
    }
}