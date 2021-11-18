import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZFrame
import org.zeromq.ZMQ

class Publisher {
    private val socket: ZMQ.Socket

    init {
        val context = ZContext()
        socket = context.createSocket(SocketType.REQ)

        socket.connect("tcp://localhost:5556")
    }
    
    fun put(/*topic: String,*/ message: String) {
        val frame = ZFrame(message)
        
        //frame.send(socket, 0)
        socket.send(message, 0)
        println("Put message: $message")
        socket.recv(0)
    }
}

fun main() {
    val publisher = Publisher()
    
    publisher.put("Hello there")
}