import org.zeromq.SocketType
import org.zeromq.ZContext
import zmq.ZMQ

fun main() {
    val context = ZContext()
    val socket = context.createSocket(SocketType.REQ)
    
    socket.connect("tcp://localhost:5559")

    val message = "Ping"
    
    socket.send(message.toByteArray(ZMQ.CHARSET), 0)

    println("Sent: [$message]")

    val reply = socket.recv(0)
    
    println("Received reply: [${String(reply, ZMQ.CHARSET)}]")

    socket.close()
    context.destroy()
}

