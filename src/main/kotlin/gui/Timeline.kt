package gui

import gnutella.peer.Peer
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JButton
import javax.swing.JPanel

class Timeline(private val gui: GUI, val peer: Peer) : JPanel(), ActionListener {
    private val logoutButton = JButton("Log out")

    init {
        add(logoutButton)
        logoutButton.addActionListener(this)

        for (post in peer.timeline())
            add(PostPanel(post))
    }

    override fun actionPerformed(event: ActionEvent) {
        when (event.source) {
            logoutButton -> {
                gui.replacePanel(Login(gui))
            }
        }
    }
}