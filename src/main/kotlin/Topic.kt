class Topic(val topicName: String) {
    var tail: Node? = null // null if list is empty
    var head: Node? = null // null if list is empty
    val subscribers = mutableMapOf<String, Node?>()


    /* Linked list Node*/
    inner class Node(var data: String) {
        var next: Node? = null
        var subCounter: Int = 0

    }

    fun isEmpty(): Boolean {
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
    }

    fun getMessage(subscriber_id: String): String? {

        // No messages for the subscriber are available
        var node = subscribers[subscriber_id] ?: return null

        if (node == tail) {
            val ret = node.data
            node.subCounter--
            subscribers[subscriber_id] = null
            return ret
        }

        //All other cases
        val ret = node.data
        node.subCounter--
        node = node.next!!
        node.subCounter++
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

        subscribers[subscriber_id]?.subCounter?.plus(-1)
        subscribers.remove(subscriber_id)
        updateHead()
    }
}