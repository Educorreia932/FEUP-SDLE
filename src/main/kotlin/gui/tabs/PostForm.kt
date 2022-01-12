package gui.tabs

import gnutella.peer.Peer
import java.awt.Color
import java.awt.Dimension
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import javax.swing.*

class PostForm(val peer: Peer) : JPanel() {
    val content = JTextArea()

    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        preferredSize = Dimension(800, 100)
        background = Color.WHITE

        resetContent()

        content.font = content.font.deriveFont(20f)

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
        
        submit.alignmentX = CENTER_ALIGNMENT
        submit.foreground = Color.decode("#1DA1F2")

        add(content)
        add(submit)
    }

    fun resetContent() {
        content.text = "What's happening?"
        content.foreground = Color.GRAY
    }
}