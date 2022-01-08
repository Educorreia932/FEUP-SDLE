package gnutella.messages

import Post
import gnutella.peer.Node
import java.util.*

class QueryHit(
    ID: UUID,
    source: Node,
    val posts: List<Post>
) : Message(ID, source) {
    override fun cloneThis(): Message {
        return QueryHit(ID, source, posts)
    }

    override fun toString(): String {
        return "QueryHit"
    }
}