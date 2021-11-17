package sdle.t2g16

import org.zeromq.SocketType
import org.zeromq.ZContext
import zmq.ZMQ

fun main(args: Array<String>) {
    val zctx = ZContext()
    val socket = zctx.createSocket(SocketType.REP)
    socket.bind("tcp://*:5555")

    while(!Thread.currentThread().isInterrupted()) {
        val reply = socket.recv(0)

        println("Received: [" + String(reply, ZMQ.CHARSET) + "]")

        val response = "Hello, world!"
        socket.send(response.toByteArray(ZMQ.CHARSET), 0)
    }

    zctx.destroy()
}

