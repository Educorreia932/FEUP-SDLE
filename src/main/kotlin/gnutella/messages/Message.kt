package gnutella.messages

open class Message(
    val address: String,
    val port: Int,
    val content: String
)