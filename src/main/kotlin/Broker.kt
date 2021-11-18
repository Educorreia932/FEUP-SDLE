import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ

fun main() {
    val context = ZContext()
    
    val frontend = context.createSocket(SocketType.ROUTER)
    val backend = context.createSocket(SocketType.DEALER)
    
    frontend.bind("tcp://*:5559")
    backend.bind("tcp://*:5560")

    //  Start the proxy
    ZMQ.proxy(frontend, backend, null);
}