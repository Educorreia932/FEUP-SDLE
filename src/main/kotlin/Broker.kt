import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ
import org.zeromq.ZMsg

class Broker {
    private val topics = mutableMapOf<String, Topic>()
    private val context = ZContext()
    private val subscriberSocket = context.createSocket(SocketType.ROUTER)
    private val publisherSocket = context.createSocket(SocketType.ROUTER)
    private val poller: ZMQ.Poller

    init {
        subscriberSocket.bind("tcp://*:5555")
        publisherSocket.bind("tcp://*:5556")

        poller = context.createPoller(2)

        poller.register(subscriberSocket, ZMQ.Poller.POLLIN)
        poller.register(publisherSocket, ZMQ.Poller.POLLIN)
    }

    fun mediate() {
        while (true) {
            val rc = poller.poll(-1)

            // Poll subscribers only if we have available workers
            if (rc == -1) {
                println("rc == -1")

                break
            }

            // Poll subscribers
            if (poller.pollin(0)) {
                val message = ZMsg.recvMsg(subscriberSocket).toArray()
                
                val action = message[2].toString()
                val topic = message[3].toString()
                val subscriberID = message[4].toString()
                
                when (action) {
                    "Subscribe" -> subscribe(topic, subscriberID)
                    "Unsubscribe" -> unsubscribe(topic, subscriberID)
                    "Get" -> TODO()
                }
            }

            // Poll publishers
            if (poller.pollin(1)) {
                val message = ZMsg.recvMsg(publisherSocket)

                println(message)
            }
        }

    }

    fun subscribe(topicName: String, subscriberID: String) {
        // Add subscriber to existing topic
        if (topics.containsKey(topicName))
            topics[topicName]?.addSubscriber(subscriberID)

        // Create topic if it doesn't exist and add subscriber
        else {
            topics[topicName] = Topic()

            topics[topicName]?.addSubscriber(subscriberID)
        }
    }

    fun unsubscribe(topic: String, subscriberID: String) {
        // Remove subscriber from topic
        topics[topic]?.removeSubscriber(subscriberID)
    }
}

fun main() {
    val broker = Broker()
    
    broker.mediate()
}
