package gnutella.peer

import User
import java.net.InetAddress

class Friend(
    user: User,
    address: InetAddress,
    port: Int,
    private val topicInterests: MutableMap<String, Float> = mutableMapOf()
) : Neighbour(user, address, port) {

    fun getInterestInUser(topic: String): Float {
        if (topicInterests[topic] != null) {
            if (topicInterests[topic] == 0f)
                return 0f
            return (topicInterests[topic])!! / (topicInterests.values.sum())
        }
        return 0f
    }

    fun addTopicInterest(topic: String) {
        if (topicInterests[topic] != null) {
            topicInterests[topic]!!.plus(1)
        } else {
            topicInterests[topic] = 1f
        }
    }

    companion object {
        @JvmStatic
        fun create(node: Node): Friend {
            return Friend(node.user, node.address, node.port)
        }
    }
}