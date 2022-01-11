package gui.tabs

import social.Post
import gnutella.peer.Peer
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util.*
import javax.swing.*
import javax.swing.border.EmptyBorder




class Timeline(val peer: Peer) : JPanel() {
    private val panel = JPanel()
    private val scrollPane = JScrollPane(panel)

    init {
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        val postForm = PostForm(peer)

        panel.add(postForm)

        for (post in peer.timeline())
            addPost(post)

        scrollPane.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED

        add(scrollPane)
    }

    private fun addPost(post: Post) {
        panel.add(PostPanel(post))

        val separator = JSeparator(SwingConstants.HORIZONTAL)

        panel.add(separator)
    }

}