package gui

import com.formdev.flatlaf.FlatLightLaf
import gnutella.peer.Peer
import java.awt.EventQueue
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants.EXIT_ON_CLOSE

class GUI(val peers: List<Peer>) {
    private val frame = JFrame("Tulicreme")

    private fun createAndShowGUI() {
        FlatLightLaf.setup()

        val login = Login(this)

        frame.contentPane.add(login)

        frame.setBounds(10, 10, 370, 600)
        frame.defaultCloseOperation = EXIT_ON_CLOSE
        frame.isResizable = false
        frame.isVisible = true
    }

    fun replacePanel(panel: JPanel) {
        frame.contentPane.removeAll()
        frame.contentPane.add(panel)
        frame.revalidate()
    }

    fun start() {
        EventQueue.invokeLater(::createAndShowGUI)
    }
}
