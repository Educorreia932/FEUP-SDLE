package gui.tabs

import gnutella.gui.tabs.PostList
import gnutella.peer.Peer
import java.awt.Color
import java.awt.Dimension
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import javax.swing.*

class PostForm(val postList: PostList, val peer: Peer) : JPanel() {
    val content = JTextArea()

    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        preferredSize = Dimension(800, 100)
        background = Color.WHITE

        val buttons = JPanel()

        clearContent()

        content.font = content.font.deriveFont(20f)

        content.addFocusListener(object : FocusListener {
            override fun focusGained(e: FocusEvent) {
                content.text = ""
                content.foreground = Color.BLACK
            }

            override fun focusLost(e: FocusEvent) {
                if (content.text == "")
                    clearContent()
            }
        })

        val submit = JButton("Submit")

        submit.addActionListener {
            if (content.text != "") {
                peer.user.createPost(content.text)

                clearContent()
                postList.refresh()
            }
        }

        submit.foreground = Color.decode("#1DA1F2")

        val refresh = JButton("Refresh")

        refresh.addActionListener {
            postList.refresh()
        }

        buttons.add(refresh)
        buttons.add(submit)

        buttons.alignmentX = CENTER_ALIGNMENT

        add(content)
        add(buttons)
    }

    fun clearContent() {
        content.text = "What's happening?"
        content.foreground = Color.GRAY
    }
}