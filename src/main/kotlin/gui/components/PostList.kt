package gui.components

import gnutella.peer.Peer
import social.Post
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.JSeparator
import javax.swing.SwingConstants

class PostList(val peer: Peer, private val search: Boolean = false) : JPanel() {
    val postPanels = mutableListOf<PostPanel>()

    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)

        refresh()
    }

    private fun addPost(post: Post) {
        val separator = JSeparator(SwingConstants.HORIZONTAL)
        val postPanel = PostPanel(peer.user, post, this)

        postPanels.add(postPanel)

        add(separator)
        add(postPanel)
    }

    fun refresh() {
        postPanels.clear()
        removeAll()

        println("Refresh: ${peer.user.storage.searchPosts}")

        val posts = if (search) peer.user.storage.searchPosts else peer.user.timeline()

        for (post in posts)
            addPost(post)

        revalidate()
        repaint()
    }
}