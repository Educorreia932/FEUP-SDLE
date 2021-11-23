import java.io.Serializable

class Topic(val topicName: String) : Serializable {
    var tail: Node? = null // null if list is empty
    var head: Node? = null // null if list is empty
    private val subscribers = mutableMapOf<String, Node?>()

    companion object{
        fun fromMap(map : Map<String, List<String>>, topicName: String): Topic{
            var topic = Topic(topicName)
            var tail: Node? = null // null if list is empty
            var head: Node? = null // null if list is empty

            for((key, value) in map){
                topic.addMessage(key)
                topic.tail!!.subList = value as MutableList<String>
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
    }

    fun addMessage(message: String) {
        val newTail = Node(message)

        if (this.isEmpty()) {
            tail = newTail
            head = newTail

            // Check all subscribers waiting for message
            for ((key, value) in subscribers) {
                if (value == null) {
                    subscribers[key] = tail
                }
            }

        } else {
            tail!!.next = newTail
            tail = tail!!.next
        }
        println(this)
    }

    fun getMessage(subscriber_id: String): String? {

        // No messages for the subscriber are available
        var node = subscribers[subscriber_id] ?: return null

        if (node == tail) {
            val ret = node.data
            node.subCounter--
            node.subList.remove(subscriber_id)
            subscribers[subscriber_id] = null
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

    fun toMap(): Map<String, List<String>>{
        //String: Message data
        //List<String>: List of subscribers subscribed to the node
        var ret = mutableMapOf<String, List<String>>()
        while(head != tail){
            ret[head!!.data] = head!!.subList
            head = head!!.next
        }
        ret[tail!!.data] = tail!!.subList
        return ret
    }





}