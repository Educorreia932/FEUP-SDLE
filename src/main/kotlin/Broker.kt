import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ
import org.zeromq.ZMsg
import java.io.*


class Broker: Serializable {
    private val topics = mutableMapOf<String, Topic>()
    private val context = ZContext()
    private val subscriberSocket = context.createSocket(SocketType.ROUTER)
    private val publisherSocket = context.createSocket(SocketType.ROUTER)

    var filePath = "topics.ser"
    val maxNumOperationsUntilSave = 2
    var numOperUntilSave = maxNumOperationsUntilSave

    init {
        subscriberSocket.bind("tcp://*:5555")
        publisherSocket.bind("tcp://*:5556")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val broker = Broker()

            loadFromFile()
            broker.mediate()
        }
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
                var msgFrame = ZMsg.recvMsg(subscriberSocket)
                val message = msgFrame.toArray()

                val action = message[2].toString()
                val topicName = message[3].toString()
                val subscriberID = if (message.size > 4) message[4].toString() else ""

                when (action) {
                    "SUBSCRIBE" -> {
                        subscribe(topicName, subscriberID)
                        val msg = ZMsg()
                        msg.add(msgFrame.first)
                        msg.addString("")
                        msg.addString("Subscribed")
                        msg.addString("Sapos")

                        msg.send(subscriberSocket)
                    }
                    "UNSUBSCRIBE" -> unsubscribe(topicName, subscriberID)
                    "GET" -> {
                        val contents = topics[topic]?.messages
                        var content = ""
                        
                        if (contents != null && contents.isNotEmpty()) {
                            content = contents.last
                        }

                        var msg = ZMsg()
                        msg.add(msgFrame.first)
                        msg.add("")

                        println("Subscriber_id: $subscriberID")
                        
                        val topic = topics[topicName]
                        
                        if (topic == null) {
                            msg.addString("No_such_topic")
                        } 
                        
                        else if (!topic.isSubscribed(subscriberID)) {
                            msg.addString("Not_subscribed")
                        } 
                        
                        else {
                            val message = topics[topicName]?.getMessage(subscriberID)

                            if (message == null) {
                                msg.addString("No_content")
                            } 
                            
                            else {
                                msg.addString("Success")
                            }

                        }
                        
                        msg.send(subscriberSocket)
                    }
                }
                
                decrementCounter()
            }

            // Poll publishers
            if (poller.pollin(1)) {
                val message = ZMsg.recvMsg(publisherSocket).toArray()

                val action = message[2].toString()
                val topic = message[3].toString()
                val content = message[4].toString()

                when (action) {
                    "PUT" -> put(topic, content)
                }
                decrementCounter()
            }
        }
    }

    private fun subscribe(topic: String, subscriberID: String) {
        // Add subscriber to existing topic
        if (topics.containsKey(topic)) {
            topics[topic]?.addSubscriber(subscriberID)
        } else { // Create topic if it doesn't exist and add subscriber
            topics[topic] = Topic(topic)

            topics[topic]?.addSubscriber(subscriberID)
        }
    }

    private fun unsubscribe(topic: String, subscriberID: String) {
        // Remove subscriber from topic
        topics[topic]?.removeSubscriber(subscriberID)
    }

    private fun put(topic: String, content: String) {
        if (!topics.containsKey(topic))
            return
        //topics[topic] = Topic()

        topics[topic]?.addMessage(content)
    }

    private fun decrementCounter(){
        numOperUntilSave--
        print(numOperUntilSave)
        if(numOperUntilSave == 0){
            print("Saving file now")
            saveToFile()
        }
    }

    fun saveToFile(){
        try {
            val fileOut = FileOutputStream(filePath)
            val out = ObjectOutputStream(fileOut)
            out.writeObject(topics)
            out.close()
            fileOut.close()
            System.out.printf("Serialized data is saved")
        } catch (i: IOException) {
            i.printStackTrace()
        }
    }

    fun loadFromFile(){

    }


}


