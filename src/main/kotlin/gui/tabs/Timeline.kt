package gui.tabs

import gnutella.gui.tabs.PostList
import social.Post
import gnutella.peer.Peer
import javax.swing.*

class Timeline(val peer: Peer) : JPanel() {
    private val panel = JPanel()
    private val scrollPane = JScrollPane(panel)

    init {
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        val postForm = PostForm(peer)
        val postList = PostList(peer)

        panel.add(postForm)
        panel.add(postList)

        scrollPane.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED

        add(scrollPane)
    }
}