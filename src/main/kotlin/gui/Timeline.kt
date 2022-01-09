package gui

import gnutella.peer.Peer
import javax.swing.JPanel

class Timeline(val peer: Peer) : JPanel() {
    init {
        setBounds(10, 10, 370, 600)

        for (post in peer.timeline())
            add(PostPanel(post))
    }
}