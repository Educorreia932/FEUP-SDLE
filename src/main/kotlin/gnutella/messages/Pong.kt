package gnutella.messages

data class Pong(
    val address: String,
    val port: Int
) : Message() {

    override fun cloneThis(): Message {
        return Pong(address, port)
    }

    override fun toString(): String {
        return "PONG"
    }
}