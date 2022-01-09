package gui

import com.formdev.flatlaf.FlatLightLaf
import gnutella.peer.Peer
import java.awt.EventQueue
import javax.swing.JFrame
import javax.swing.WindowConstants.EXIT_ON_CLOSE
import kotlin.math.log


class GUI(private val peers: List<Peer>) {
    private fun createAndShowGUI() {
        FlatLightLaf.setup()

        val frame = JFrame("Tulicreme")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        val login = Login(frame, peers)

        frame.contentPane.add(login)

        frame.setBounds(10, 10, 370, 600)
        frame.defaultCloseOperation = EXIT_ON_CLOSE
        frame.isResizable = false
        frame.isVisible = true
    }

    fun start() {
        EventQueue.invokeLater(::createAndShowGUI)
    }
}
