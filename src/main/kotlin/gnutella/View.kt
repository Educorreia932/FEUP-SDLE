package gnutella

import org.graphstream.graph.Graph
import org.graphstream.graph.implementations.SingleGraph

class View {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            System.setProperty("org.graphstream.ui", "swing")

            val peers = Network().peers
            
            Thread.sleep(1000) // TODO: Needed, because graph is not updating in real time yet
            
            val graph: Graph = SingleGraph("Network")

            for (peer in peers) {
                val node = graph.addNode(peer.port.toString())

                node.setAttribute("ui.label", "Peer ${peer.user.username}")
            }

            for (peer in peers) {
                for (neighbour in peer.neighbours) {
                    graph.addEdge(
                        "${peer.port}-${neighbour.port}",
                        peer.port.toString(),
                        neighbour.port.toString(),
                        true
                    )
                }
                    
            }

            graph.display()
        }
    }
}