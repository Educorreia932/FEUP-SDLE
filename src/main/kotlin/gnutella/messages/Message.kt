package gnutella.messages

open class Message(
    val address: String,
    val port: Int,
    val content: String = ""
) {
    companion object {
        fun fromBytes(bytes: ByteArray): Message {
            val messageFields = String(bytes).split("|")

            return when (messageFields[0]) {
                "PING" -> Ping(messageFields[1], messageFields[2].toInt())
                "PONG" -> Pong(messageFields[1], messageFields[2].toInt())
                "QUERY" -> Query()
                "QUERY_HIT" -> QueryHit()
                
                else -> {
                    Message("", -1, "")
                }
            }

        }
    }
}