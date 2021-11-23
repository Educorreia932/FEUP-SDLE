import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ
import org.zeromq.ZMsg
import java.io.*

import java.io.ObjectInputStream
import java.io.FileInputStream
import java.lang.Exception


class Broker : Serializable {
    private val topics = mutableMapOf<String, Topic>()

    @Transient
    private var context = ZContext()

    @Transient
    private var subscriberSocket = context.createSocket(SocketType.ROUTER)

    @Transient
    private var publisherSocket = context.createSocket(SocketType.ROUTER)

    @Transient
    var maxNumOperationsUntilSave = 2

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
        maxNumOperationsUntilSave = 2
        numOperUntilSave = maxNumOperationsUntilSave
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val broker: Pair<Broker, Boolean> = loadFromFile()


            if (broker.second) {
                //broker.first.altConstructor()
                for((k, v) in broker.first.topics){
                    println("Topic $k:\n")
                    v.printTopic()
                }
            }
            broker.first.mediate()
        }

        @JvmStatic
        fun loadFromFile(): Pair<Broker, Boolean> {
            val broker: Broker?
            val map: MutableMap<String, Map<String, List<String>>>

            try {
                val file = File(filePath)

                if (!file.isFile)
                    return Pair(Broker(), false)

                val fileIn = FileInputStream(filePath)
                val objIn = ObjectInputStream(fileIn)
                broker = Broker()
                map = objIn.readObject() as MutableMap<String, Map<String, List<String>>>
                for((k, v) in map){
                    broker.topics[k] = Topic.fromMap(v, k)
                }
                objIn.close()
                fileIn.close()
            } catch (i: IOException) {
                i.printStackTrace()

                return Pair(Broker(), false)
            } catch (c: ClassNotFoundException) {
                println("Class not found")
                c.printStackTrace()

                return Pair(Broker(), false)
            }

            println("Deserialized Broker...")
            println("Num topics: ${broker.topics.count()}")

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
                val messageFrame = ZMsg.recvMsg(subscriberSocket)
                val message = messageFrame.toArray()

                val action = message[2].toString()
                val topicName = message[3].toString()
                val subscriberID = if (message.size > 4) message[4].toString() else ""

                when (action) {
                    "SUBSCRIBE" -> {
                        // Subscribe to topic
                        subscribe(topicName, subscriberID)

                        val msg = ZMsg()

                        msg.add(messageFrame.first)
                        msg.addString("")
                        msg.addString("Subscribed")
                        msg.addString(topicName)

                        msg.send(subscriberSocket)
                    }
                    "UNSUBSCRIBE" -> unsubscribe(topicName, subscriberID)
                    "GET" -> {
                        val zmsg = ZMsg()

                        zmsg.add(messageFrame.first)
                        zmsg.add("")

                        println("Subscriber ID: $subscriberID")

                        val topic = topics[topicName]
                        if (topic == null || !topic.isSubscribed(subscriberID)) {
                            zmsg.addString("Not_subscribed")
                        } else {

                            val content = topics[topicName]?.getMessage(subscriberID)
                            if (content == null) {
                                zmsg.addString("Empty_Topic")
                            } else {
                                zmsg.addString(content)
                            }
                        }

                        zmsg.send(subscriberSocket)
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

                        val zmsg = ZMsg()

                        zmsg.add(msgFrame.first)
                        zmsg.addString("")
                        zmsg.addString("Published")
                        zmsg.addString(topic)
                        zmsg.addString(content)

                        zmsg.send(publisherSocket)
                    }
                }
            }
        }
    }

    private fun subscribe(topic: String, subscriberID: String) {
        // Add subscriber to existing topic
        if (topics.containsKey(topic)) {
            topics[topic]?.addSubscriber(subscriberID)
        }

        // Create topic if it doesn't exist and add subscriber
        else {
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
        println(topics[topic])

    }

    private fun decrementCounter() {
        numOperUntilSave--

        println("Until save: $numOperUntilSave")
        if (numOperUntilSave == 0) {
            println("Saving file now")
            saveToFile()
            numOperUntilSave = maxNumOperationsUntilSave
        }
    }

    private fun saveToFile() {
        try {
            var topicInfo: MutableMap<String, Map<String, List<String>>> = mutableMapOf()
            // The outer map's keys are the topic names and it's values are the information from each topic
            // The inner map's keys are the messages
            // The map's list is a list of the subscriber ids which are in that message

            for((key, value) in topics){
                topicInfo[key]= value.toMap()
            }

            val fileOut = FileOutputStream(filePath)
            val out = ObjectOutputStream(fileOut)

            out.writeObject(topicInfo)
            out.close()
            fileOut.close()
            println("Serialized data is saved")
            for((k, v) in topics){
                //println("Topic $k:\n")
                //v.printTopic()
            }
        } catch (i: IOException) {
            i.printStackTrace()
        }
    }
}
