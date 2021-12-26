package gnutella.messages

class Ping(
    sourceAddress: String,
    sourcePort: Int
) : Message() {
    override fun toString(): String {
        return "PING"
    }
}