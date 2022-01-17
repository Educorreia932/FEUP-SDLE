package gui

import com.formdev.flatlaf.FlatLightLaf
import gnutella.peer.Peer
import gui.frames.Application
import gui.frames.Login
import java.awt.EventQueue
import javax.swing.UIManager

class GUI(private val peers: List<Peer>) {
    private fun createAndShowGUI() {
        FlatLightLaf.setup()

        UIManager.put("Component.focusWidth", 1)
        UIManager.put("Button.arc", 999)

        Login()
//        Application(peers[7])
    }

    fun start() {
        EventQueue.invokeLater(::createAndShowGUI)
    }
}
