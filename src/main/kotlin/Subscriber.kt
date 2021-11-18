import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ

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
        socket.send("Subscribe: $topic $id", 0)
    }
    
    fun unsubscribe(topic: String) {
        socket.send("Unsubscribe: $topic $id", 0)
    }
    
    fun get(topic: String) {
        socket.recv()
    }
}

fun main() {
    
}