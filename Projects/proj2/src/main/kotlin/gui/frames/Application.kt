package gui.frames

import gnutella.peer.Peer
import gui.tabs.Search
import gui.tabs.Timeline
import java.awt.GridLayout
import javax.swing.JFrame
import javax.swing.JTabbedPane

class Application(val peer: Peer) : JFrame() {
    init {
        title = "Tulicreme | ${peer.user.name}"
        layout = GridLayout()

        val tabbedPane = JTabbedPane()

        tabbedPane.addTab("Timeline", Timeline(peer))
        tabbedPane.addTab("Search", Search(peer))

        add(tabbedPane)

        pack()
        
        extendedState = MAXIMIZED_BOTH
        isVisible = true
    }
}