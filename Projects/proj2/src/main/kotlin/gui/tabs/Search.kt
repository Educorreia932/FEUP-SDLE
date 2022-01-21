package gui.tabs

import gui.components.PostForm
import gnutella.peer.Peer
import gui.components.PostList
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.JScrollPane

class Search(val peer: Peer) : JPanel() {
    private val panel = JPanel()
    private val scrollPane = JScrollPane(panel)

    init {
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        val postList = PostList(peer, true)
        val postForm = PostForm(postList, peer, true)

        panel.add(postForm)
        panel.add(postList)

        scrollPane.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED

        add(scrollPane)
    }
}