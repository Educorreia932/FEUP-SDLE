import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ
import org.zeromq.ZMsg
import java.io.*
import java.io.ObjectInputStream

import java.io.FileInputStream


class Broker : Serializable {
    private val topics = mutableMapOf<String, Topic>()

    @Transient
    private var context = ZContext()

    @Transient
    private var subscriberSocket = context.createSocket(SocketType.ROUTER)

    @Transient
    private var publisherSocket = context.createSocket(SocketType.ROUTER)

    @Transient
    val maxNumOperationsUntilSave = 2

    @Transient
    var numOperUntilSave = maxNumOperationsUntilSave

    init {
        altConstructor()
    }

    fun altConstructor() {
        context = ZContext()
        subscriberSocket = context.createSocket(SocketType.ROUTER)
        publisherSocket = context.createSocket(SocketType.ROUTER)
        subscriberSocket.bind("tcp://*:5555")
        publisherSocket.bind("tcp://*:5556")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val broker: Pair<Broker, Boolean> = loadFromFile()
            if (broker.second) {
                broker.first.altConstructor()
            }
            broker.first.mediate()
        }

        @JvmStatic
        fun loadFromFile(): Pair<Broker, Boolean> {
            val broker: Broker?
            try {
                val file = File(filePath)
                if (!file.isFile)
                    return Pair(Broker(), false)

                val fileIn = FileInputStream(filePath)
                val `in` = ObjectInputStream(fileIn)
                broker = `in`.readObject() as Broker
                `in`.close()
                fileIn.close()
            } catch (i: IOException) {
                i.printStackTrace()
                return Pair(Broker(), false)
            } catch (c: ClassNotFoundException) {
                println("Employee class not found")
                c.printStackTrace()
                return Pair(Broker(), false)
            }

            println("Deserialized Broker...")
            println("Num topics: " + broker.topics.count())
            return Pair(broker, true)
        }

        @Transient
        const val filePath = "topics.ser"
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
                val msgFrame = ZMsg.recvMsg(subscriberSocket)
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
                        msg.addString(topicName)

                        msg.send(subscriberSocket)
                    }
                    "UNSUBSCRIBE" -> unsubscribe(topicName, subscriberID)
                    "GET" -> {
                        val msg = ZMsg()
                        msg.add(msgFrame.first)
                        msg.add("")

                        println("Subscriber_id: $subscriberID")

                        val topic = topics[topicName]

                        if (topic == null) {
                            msg.addString("No_such_topic")
                        } else if (!topic.isSubscribed(subscriberID)) {
                            msg.addString("Not_subscribed")
                        } else {
                            val content = topics[topicName]?.getMessage(subscriberID)

                            if (content == null) {
                                msg.addString("No_content")
                            } else {
                                msg.addString("Success")
                            }

                        }

                        msg.send(subscriberSocket)
                        decrementCounter()
                    }
                }
            }

            // Poll publishers
            if (poller.pollin(1)) {
                val msgFrame = ZMsg.recvMsg(publisherSocket)
                val message = msgFrame.toArray()

                val action = message[2].toString()
                val topic = message[3].toString()
                val content = message[4].toString()

                when (action) {
                    "PUT" -> {
                        put(topic, content)
                        decrementCounter()

                        val msg = ZMsg()
                        msg.add(msgFrame.first)
                        msg.addString("")
                        msg.addString("Published")
                        msg.addString(topic)
                        msg.addString(content)

                        msg.send(publisherSocket)
                    }
                }
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

        topics[topic]?.addMessage(content)
    }

    private fun decrementCounter() {
        numOperUntilSave--
        print(numOperUntilSave)
        if (numOperUntilSave == 0) {
            print("Saving file now")
            saveToFile()
            numOperUntilSave = maxNumOperationsUntilSave
        }
    }

    private fun saveToFile() {
        try {
            val fileOut = FileOutputStream(filePath)
            val out = ObjectOutputStream(fileOut)
            out.writeObject(this)
            out.close()
            fileOut.close()
            System.out.printf("Serialized data is saved")
        } catch (i: IOException) {
            i.printStackTrace()
        }
    }
}


