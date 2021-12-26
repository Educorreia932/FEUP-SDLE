package gnutella.messages

data class Pong(
    val address: String,
    val port: Int
) : Message() {
    override fun toString(): String {
        return "PONG"
    }
}