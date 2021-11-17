package sdle.t2g16

import org.zeromq.SocketType
import org.zeromq.ZContext
import zmq.ZMQ

fun main(args: Array<String>) {
    val zctx = ZContext()
    val socket = zctx.createSocket(SocketType.REQ)
    socket.connect("tcp://localhost:5555")

    val response = "Hello, world!"
    socket.send(response.toByteArray(ZMQ.CHARSET), 0)

    println("Sent: [$response]")

    val reply = socket.recv(0)
    println("Sent: [" + String(reply, ZMQ.CHARSET) + "]")

    zctx.destroy()
}

