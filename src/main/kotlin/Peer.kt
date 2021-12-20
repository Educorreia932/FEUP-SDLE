class Peer(
    private val username: String
) {
    private var neighbours = mutableListOf<Peer>()

    fun connect(peer: Peer) {
        if (this != peer && peer !in neighbours) {
            neighbours.add(peer)
            peer.connect(this)
        }
    }

    fun ping() {

    }

    fun pong() {

    }

    override fun equals(other: Any?): Boolean {
        return username == (other as Peer).username
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + neighbours.hashCode()
        
        return result
    }
}