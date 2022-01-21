package gui.tabs

import gnutella.peer.Peer
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.JScrollPane

class Profile(val peer: Peer): JPanel() {
    private val panel = JPanel()
    private val scrollPane = JScrollPane(panel)

    init {
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        add(panel)
    }
}