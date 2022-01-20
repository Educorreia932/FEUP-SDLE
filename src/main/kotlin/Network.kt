import gnutella.HostCache
import social.User
import gnutella.peer.Peer
import gui.GUI
import org.graphstream.graph.Graph
import org.graphstream.graph.implementations.SingleGraph
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun main() {
	System.setProperty("org.graphstream.ui", "swing")

	val graph: Graph = SingleGraph("Network")
	graph.setAttribute(
		"ui.stylesheet", 
		"""
			node {
				size: 15px, 15px;
				shape: circle;
				fill-color: green;
				stroke-mode: plain;
				stroke-color: yellow;
			}
		""".trimIndent()
	);

	HostCache()

//	graph.display()

	val A = Peer(User("dferreira", "David Ferreira"), graph = graph)
	val B = Peer(User("ecorreia", "Eduardo Correia"), graph = graph)
	val C = Peer(User( "jcardoso", "João Cardoso"), graph = graph)
	val D = Peer(User( "rfontao", "Ricardo Fontão"), graph = graph)

	A.connect()
	B.connect()
	C.connect()
	D.connect()
	
	A.user.follow(B.user)
	C.user.follow(A.user)

	val peers = mutableListOf(A, B, C, D)
	
//    Executors.newScheduledThreadPool(1).scheduleAtFixedRate(
//        {
//            val node = peers.random()
//            node.stop()
//            peers.remove(node)
//        }, 7, 5, TimeUnit.SECONDS
//    )

	Thread.sleep(200)

	val gui = GUI(peers, graph)

	gui.start()
	Thread.sleep(5000)
}