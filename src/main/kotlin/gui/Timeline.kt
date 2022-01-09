package gui

import gnutella.peer.Peer
import java.awt.Container
import javax.swing.JFrame

class Timeline(val peer: Peer): JFrame() {
    private val container: Container = contentPane

    init {
        title = "Tulicreme | Timeline"
        isVisible = true
        defaultCloseOperation = EXIT_ON_CLOSE
        isResizable = false

        setBounds(10, 10, 370, 600)
        
        for (post in peer.timeline())
            container.add(PostPanel(post))
    }
}