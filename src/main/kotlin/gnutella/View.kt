package gnutella

import org.graphstream.graph.Graph
import org.graphstream.graph.implementations.SingleGraph

class View {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            System.setProperty("org.graphstream.ui", "swing")

            val peers = Network().peers
            val graph: Graph = SingleGraph("Tulicreme Network")

            for (peer in peers) {
                val node = graph.addNode(peer.port.toString())

                node.setAttribute("ui.label", "Peer ${peer.port}");
            }

            for (peer in peers) {
                for (neighbour in peer.neighbours)
                    graph.addEdge(
                        "${peer.port}-${neighbour.port}",
                        peer.port.toString(),
                        neighbour.port.toString(),
                        true
                    )
            }

            graph.display();
        }
    }
}