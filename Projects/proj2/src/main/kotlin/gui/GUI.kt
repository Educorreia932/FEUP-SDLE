package gui

import com.formdev.flatlaf.FlatLightLaf
import gnutella.peer.Peer
import gui.frames.Login
import org.graphstream.graph.Graph
import java.awt.EventQueue
import javax.swing.UIManager

class GUI(private val peers: MutableList<Peer>, private val graph: Graph) {
    private fun createAndShowGUI() {
        FlatLightLaf.setup()

        UIManager.put("Component.focusWidth", 1)
        UIManager.put("Button.arc", 999)

        Login(peers, graph = graph)
    }

    fun start() {
        EventQueue.invokeLater(::createAndShowGUI)
    }
}
