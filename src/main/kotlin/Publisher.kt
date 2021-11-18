import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ

class Publisher {
    private val socket: ZMQ.Socket

    init {
        val context = ZContext()
        socket = context.createSocket(SocketType.REQ)

        socket.connect("tcp://localhost:5556")
    }
    
    fun put(topic: String, message: String) {
        socket.send(message, 0)
        println("Put message: $message")
        socket.recv(0)
    }
}

fun main() {
    val publisher = Publisher()
    
    publisher.put("Sapos", "Rãs")
}