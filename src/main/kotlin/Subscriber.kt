import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ
import org.zeromq.ZMsg

class Subscriber(
    private val id: String
) {
    private val socket: ZMQ.Socket

    init {
        val context = ZContext()
        socket = context.createSocket(SocketType.REQ)

        socket.connect("tcp://localhost:5555")
    }

    fun subscribe(topic: String) {
        println("SUBSCRIBE|$topic|$id")

        val message = ZMsg()

        message.addString("SUBSCRIBE")
        message.addString(topic)
        message.addString(id)

        message.send(socket)

        val reply = ZMsg.recvMsg(socket)

        println(reply)
    }

    fun unsubscribe(topic: String) {
        println("UNSUBSCRIBE|$topic|$id")

        val message = ZMsg()

        message.addString("UNSUBSCRIBE")
        message.addString(topic)
        message.addString(id)

        message.send(socket)

        val reply = ZMsg.recvMsg(socket)
        println(reply)
    }

    // Returns message data or null if not subscribed to a topic
    fun get(topic: String): String? {
        println("Sending GET on Topic: $topic")

        var reply: String? = null
        var success: Boolean
        do {

            if (reply != null) {
                Thread.sleep(1_000)
            }

            val message = ZMsg()
            message.addString("GET")
            message.addString(topic)
            message.addString(id)

            message.send(socket)

            reply = ZMsg.recvMsg(socket).first.toString()
            println(reply)

            success = when (reply) {
                "Not_Subscribed" -> return null
                "Empty_Topic" -> {
                    println("GET: No new Messages. Retrying...")
                    false
                }
                else -> true
            }

        } while (!success)

        return reply
    }
}
