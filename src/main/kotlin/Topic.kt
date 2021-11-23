import java.io.Serializable

class Topic(val topicName: String) : Serializable {
    var tail: Node? = null // null if list is empty
    var head: Node? = null // null if list is empty
    private val subscribers = mutableMapOf<String, Node?>()

    companion object{
        fun fromMap(mapPair : Pair<Map<String, List<String>>, List<String>>, topicName: String): Topic{
            var topic = Topic(topicName)
            var tail: Node? = null // null if list is empty
            var head: Node? = null // null if list is empty

            var map = mapPair.first
            for((key, value) in map){
                topic.addMessage(key)
                topic.tail!!.subList = value as MutableList<String>
                for(i in value){
                    topic.subscribers[i] = topic.tail
                }
            }

            //Add subscribers which don't belong to any node (ones who don't have anything to read but are subscribed)
            var subscribersAtEndOfList = mapPair.second
            for(sub in subscribersAtEndOfList){
                topic.subscribers[sub] = null
            }

            return topic
        }
    }

    /* Linked list Node*/
    inner class Node(var data: String) : Serializable {
        var next: Node? = null
        var subCounter: Int = 0
        var subList = mutableListOf<String>()
    }

    private fun isEmpty(): Boolean {
        return tail == null
    }

    private fun updateHead() {
        if (head == null) {
            return
        }

        if (head!!.subCounter > 0) {
            return
        }
        while (head!!.next != null && head!!.subCounter == 0) {
            head = head!!.next
        }
        //If the head and the tail are the same node and noone needs that node's message anymore
        if(head != null && head!!.subCounter == 0){
            head = null
            tail = null
        }
    }

    fun addMessage(message: String) {
        val newTail = Node(message)

        if (this.isEmpty()) {
            tail = newTail
            head = newTail



        } else {
            tail!!.next = newTail
            tail = tail!!.next
        }

        // Check all subscribers waiting for message
        for ((key, value) in subscribers) {
            if (value == null) {
                subscribers[key] = tail
                tail!!.subCounter++
                tail!!.subList.add(key)
            }
        }
    }

    fun getMessage(subscriber_id: String): String? {

        // No messages for the subscriber are available
        var node = subscribers[subscriber_id] ?: return null

        if (node == tail) {
            val ret = node.data
            node.subCounter--
            node.subList.remove(subscriber_id)
            subscribers[subscriber_id] = null
            updateHead()
            return ret
        }

        //All other cases
        val ret = node.data
        node.subCounter--
        node.subList.remove(subscriber_id)
        node = node.next!!
        node.subCounter++
        node.subList.add(subscriber_id)
        subscribers[subscriber_id] = node

        //Update head
        updateHead()
        return ret
    }

    fun addSubscriber(subscriber_id: String) {
        subscribers[subscriber_id] = null
    }

    fun isSubscribed(subscriber_id: String): Boolean {
        return subscribers.containsKey(subscriber_id)
    }

    fun removeSubscriber(subscriber_id: String) {
        subscribers[subscriber_id]?.subList?.remove(subscriber_id)
        subscribers[subscriber_id]?.subCounter?.plus(-1)
        subscribers.remove(subscriber_id)

        updateHead()
    }

    override fun toString(): String {

        if (isEmpty()) {
            return "EMPTY"
        }

        var ret = ""
        var curNode: Node? = head
        while (curNode!!.next != null) {
            ret = ret.plus(" ${curNode.data}")
            curNode = curNode.next!!
        }

        return ret
    }

    fun toMap(): Pair<Map<String, List<String>>, List<String>>{
        //String: Message data
        //List<String>: List of subscribers subscribed to the node
        var ret = mutableMapOf<String, List<String>>()
        var node: Node? = head

        while(node != null){
            ret[node.data] = node.subList
            node = node.next
        }

        var subsInLastNode: MutableList<String> = mutableListOf()
        for ((key, value) in subscribers) {
            if (value == null) {
                subsInLastNode.add(key)
            }
        }

        return Pair(ret, subsInLastNode)
    }

    fun printTopic(){
        println("Topic name: " + topicName)
        println("\n")
        println("Subscribers: ")
        for((k, v) in subscribers){
            println(k + " - " + v?.data)
        }
        println("\n")
        println("Nodes:")
        var node : Node? = head
        while(node != null){
            println(node.data)
            println(node.subCounter)
            println(" Subscriber in node list:")
            for(k in node.subList){
                println(" $k")
            }
            node = node.next
        }
        println("\n")

    }


}