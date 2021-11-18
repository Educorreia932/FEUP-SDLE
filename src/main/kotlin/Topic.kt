import java.util.LinkedList

class Topic {
    private val subscribers = LinkedList<String>()
    private val messages = mutableListOf<String>()
    
    fun addSubscriber(subscriberID: String) {
        subscribers.add(subscriberID)
    }
    
    fun removeSubscriber(subscriberID: String) {
        subscribers.remove(subscriberID)
    }
}