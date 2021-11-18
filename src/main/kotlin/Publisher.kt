import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ
import org.zeromq.ZMsg

class Publisher {
    private val socket: ZMQ.Socket

    init {
        val context = ZContext()
        socket = context.createSocket(SocketType.REQ)

        socket.connect("tcp://localhost:5556")
    }
    
    fun put(topic: String, content: String) {
        val message = ZMsg()
        
        println("PUT|$topic|$content")

        message.addString("PUT")
        message.addString(topic)
        message.addString(content)
  
        message.send(socket)
        
        socket.recv(0)
    }
}

fun main() {
    val publisher = Publisher()
    
    publisher.put("Sapos", "Rãs")
}