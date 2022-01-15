package gui.components

import gnutella.peer.Peer
import social.Post
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.JSeparator
import javax.swing.SwingConstants

class PostList(val peer: Peer, val search: Boolean = false) : JPanel() {
    val posts = mutableListOf<PostPanel>()

    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)

        refresh()
    }

    private fun addPost(post: Post) {
        val separator = JSeparator(SwingConstants.HORIZONTAL)
        val postPanel = PostPanel(peer.user, post, this)

        posts.add(postPanel)

        add(separator)
        add(postPanel)
    }

    fun refresh() {
        posts.clear()
        removeAll()

        if (search)
            for (post in peer.user.storage.searchPosts)
                addPost(post)
        
        else
            for (post in peer.timeline())
                addPost(post)

        revalidate()
        repaint()
    }
}