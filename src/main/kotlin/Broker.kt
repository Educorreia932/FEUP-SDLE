import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ
import org.zeromq.ZMsg

class Broker {
    private val topics = mutableMapOf<String, Topic>()
    private val context = ZContext()
    private val subscriberSocket = context.createSocket(SocketType.ROUTER)
    private val publisherSocket = context.createSocket(SocketType.ROUTER)

    init {
        subscriberSocket.bind("tcp://*:5555")
        publisherSocket.bind("tcp://*:5556")
    }

    fun mediate() {
        val poller = context.createPoller(2)

        poller.register(subscriberSocket, ZMQ.Poller.POLLIN)
        poller.register(publisherSocket, ZMQ.Poller.POLLIN)

        while (true) {
            val rc = poller.poll(-1)

            // Poll subscribers only if we have available workers
            if (rc == -1)
                break

            // Poll subscribers
            if (poller.pollin(0)) {
                val message = ZMsg.recvMsg(subscriberSocket).toArray()

                val action = message[2].toString()
                val topic = message[3].toString()
                val subscriberID = if (message.size > 4) message[4].toString() else ""

                when (action) {
                    "SUBSCRIBE" -> subscribe(topic, subscriberID)
                    "UNSUBSCRIBE" -> unsubscribe(topic, subscriberID)
                    "GET" -> {
                        val content = topics[topic]?.messages?.last
                        
                        subscriberSocket.send(content)
                    }
                }
            }

            // Poll publishers
            if (poller.pollin(1)) {
                val message = ZMsg.recvMsg(publisherSocket).toArray()

                val action = message[2].toString()
                val topic = message[3].toString()
                val content = message[4].toString()
                
                when (action)  {
                    "PUT" -> put(topic, content)
                }
            }
        }
    }

    private fun subscribe(topic: String, subscriberID: String) {
        // Add subscriber to existing topic
        if (topics.containsKey(topic))
            topics[topic]?.addSubscriber(subscriberID)

        // Create topic if it doesn't exist and add subscriber
        else {
            topics[topic] = Topic()

            topics[topic]?.addSubscriber(subscriberID)
        }
    }

    private fun unsubscribe(topic: String, subscriberID: String) {
        // Remove subscriber from topic
        topics[topic]?.removeSubscriber(subscriberID)
    }
    
    private fun put(topic: String, content: String) {
        if (!topics.containsKey(topic))
            topics[topic] = Topic()
        
        topics[topic]?.addMessage(content)
    }
}

fun main() {
    val broker = Broker()

    broker.mediate()
}
