import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ

class Broker {
    private var subscribers: MutableMap<String, MutableSet<Subscriber>> = hashMapOf()

    init {
        println("Broker is now running...")
        
        val context = ZContext()

        val subscriberSocket = context.createSocket(SocketType.ROUTER)
        val publisherSocket = context.createSocket(SocketType.ROUTER)

        println("Binding sockets")
        subscriberSocket.bind("tcp://*:5555")
        publisherSocket.bind("tcp://*:5556")

        val poller = context.createPoller(2)

        println("Registering for subscriber")
        poller.register(subscriberSocket, ZMQ.Poller.POLLIN)
        println("Registering for publisher")
        poller.register(publisherSocket, ZMQ.Poller.POLLIN)
        
        while (true) {
            println("Stated while loop")
//            val message = ZMsg.recvMsg(publisherSocket)
            val rc = poller.poll(-1)
            println("Poll successful")
            //  Poll frontend only if we have available workers
            if (rc == -1) {
                println("rc == -1")
                break //  Interrupted
            }
            
            if (poller.pollin(0)) {
                println("receiving 0")
                val message = publisherSocket.recv(0)

                println(message)
            }

            if (poller.pollin(1)) {
                println("receiving 1")
                val message = publisherSocket.recv(0)

                println(message)
            }
        }
    }

    fun subscribe(topic: String, subscriberID: Subscriber) {
        // Add subscriber to existing topic
        if (subscribers.containsKey(topic))
            subscribers[topic]?.add(subscriberID)

        // Create topic if it doesn't exist and add subscriber
        else
            subscribers[topic] = mutableSetOf(subscriberID)
    }

    fun unsubscribe(topic: String, subscriberID: Subscriber) {
        // Remove subscriber from topic
        subscribers[topic]?.remove(subscriberID)
    }
}

fun main() {
    val broker = Broker()
}
