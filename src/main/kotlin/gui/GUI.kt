package gui

import com.formdev.flatlaf.FlatLightLaf
import gnutella.peer.Peer
import java.awt.EventQueue

class GUI(private val peers: List<Peer>) {
    private fun createAndShowGUI() {
        FlatLightLaf.setup()

        val frame = Login(peers)

        frame.isVisible = true
    }
    
    fun start() {
        EventQueue.invokeLater(::createAndShowGUI)
    }
}
