package gnutella

class Constants {
    companion object {
        const val MAX_HOPS: Int = 3
        const val CONNECTION_TIMEOUT_MILIS = 10000
        const val CONNECTION_REQUEST_STRING = "TULICREME_CONNECT" // Cannot contain CONNECTION_MESSAGE_SEPARATOR
        const val CONNECTION_ACCEPTANCE_STRING = "TULICREME_OK" // Cannot contain CONNECTION_MESSAGE_SEPARATOR
        const val CONNECTION_MESSAGE_SEPARATOR = "|"
    }
}
