package gnutella.messages

class Ping(
    sourceAddress: String,
    sourcePort: Int
) : Message(sourceAddress, sourcePort, "PING")