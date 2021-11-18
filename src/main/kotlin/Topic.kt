import java.util.LinkedList

class Topic {
    private val subscribers = mutableSetOf<String>()
    val messages = LinkedList<String>()

    fun addSubscriber(subscriberID: String) {
        subscribers.add(subscriberID)
    }
    
    fun removeSubscriber(subscriberID: String) {
        subscribers.remove(subscriberID)
    }
    
    fun addMessage(content: String) {
        messages.add(content)
    }
}