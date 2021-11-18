import org.zeromq.SocketType
import org.zeromq.ZContext
import zmq.ZMQ

fun main() {
    val context = ZContext()
    val socket = context.createSocket(SocketType.REP)
    
    socket.connect("tcp://*:5560")

    while (!Thread.currentThread().isInterrupted) {
        val message = socket.recv(0)

        println("Received: [${String(message, ZMQ.CHARSET)}]")

        val reply = "Pong"
        
        socket.send(reply.toByteArray(ZMQ.CHARSET), 0)

        println("Sent: [$reply]")
    }

    socket.close()
    context.destroy()
}

