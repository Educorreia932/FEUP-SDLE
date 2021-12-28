package gnutella.messages

data class Pong(
    val address: String,
    val port: Int
) : Message() {

    override fun to(address: String, port: Int): Message {
        val pong = Pong(address, port)
        pong.destinationPort = port
        pong.destinationAddress = address
        return pong
    }

    override fun toString(): String {
        return "PONG"
    }
}