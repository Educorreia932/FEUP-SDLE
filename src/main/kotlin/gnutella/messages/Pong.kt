package gnutella.messages

data class Pong(
    val destinationAddress: String,
    val destinationPort: Int
) : Message(destinationAddress, destinationPort,  "PONG")