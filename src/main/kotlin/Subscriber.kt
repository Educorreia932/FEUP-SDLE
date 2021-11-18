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
        val message = ZMsg()
        
        message.addString("Subscribe")
        message.addString(topic)
        message.addString(id)
        
        message.send(socket)
        
        socket.recv(0)
    }
    
    fun unsubscribe(topic: String) {
        socket.send("Unsubscribe|$topic|$id", 0)
    }
    
    fun get(topic: String) {
        socket.send("Get|$topic")
        
        val message = socket.recv()
        println("Subscriber with ID $id")
    }
}

fun main() {
    val subscriber = Subscriber("1")
    
    subscriber.subscribe("Sapos")
    // subscriber.get("Sapos")
}