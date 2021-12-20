fun main() {
    val peers = mutableListOf<Peer>()

    for (i in 1..5)
        peers.add(Peer(i.toString()))

    // Connect peers in star pattern
    peers[0].connect(peers[2])
    peers[0].connect(peers[3])
    peers[1].connect(peers[3])
    peers[1].connect(peers[4])
    peers[2].connect(peers[4])
}