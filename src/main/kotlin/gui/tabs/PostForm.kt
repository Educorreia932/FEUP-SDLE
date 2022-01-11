package gui.tabs

import gnutella.peer.Peer
import java.awt.Color
import java.awt.Dimension
import java.awt.GridBagLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JTextField

class PostForm(val peer: Peer) : JPanel() {
    val content = JTextField()

    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        preferredSize = Dimension(800, 100)

        resetContent()

        content.addFocusListener(object : FocusListener {
            override fun focusGained(e: FocusEvent) {
                content.text = ""
                content.foreground = Color.BLACK
            }

            override fun focusLost(e: FocusEvent) {
                if (content.text == "")
                    resetContent()
            }
        })

        val submit = JButton("Submit")

        submit.addActionListener {
            if (content.text != "") {
                peer.user.createPost(content.text)

                resetContent()
            }
        }

        add(content)
        add(submit)
    }

    fun resetContent() {
        content.text = "What's happening?"
        content.foreground = Color.GRAY
    }
}