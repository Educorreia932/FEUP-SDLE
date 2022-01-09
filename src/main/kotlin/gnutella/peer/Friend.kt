package gnutella.peer

import User
import java.net.InetAddress

class Friend(
    user: User,
    address: InetAddress,
    port: Int,
    val topicInterests: MutableMap<String, Int> = mutableMapOf()
) : Neighbour(user, address, port){

    fun getInterestInUser(topic: String): Float{
        if(topicInterests[topic] != null){
            if(topicInterests[topic] == 0)
                return 0f
            return (topicInterests[topic] as Float) / (topicInterests.values.sum() as Float)
        }
        return 0f
    }

    fun addTopicInterest(topic: String){
        if(topicInterests[topic] != null){
            topicInterests[topic]!!.plus(1)
        }
        topicInterests[topic] = 1
    }

    companion object{
        @JvmStatic
        fun create(node: Node): Friend{
            val friend = Friend(node.user, node.address, node.port)
            return friend
        }
    }
}