package gui

import Post
import User
import gnutella.peer.Peer
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util.*
import javax.swing.*


class Timeline(private val gui: GUI, val peer: Peer) : JPanel(), ActionListener {
    private val logoutButton = JButton("Log out")
    private val postButton = JButton("Post")
    private val postText = JTextArea(12, 24)
    private var postLabel = JLabel("Post:")
    private val panel = JPanel()
    private val scrollPane = JScrollPane(panel)
    private var posts = mutableListOf<PostPanel>()

    init {
        add(logoutButton)

        panel.layout = BoxLayout(panel, BoxLayout.PAGE_AXIS)

        logoutButton.addActionListener(this)
        postButton.addActionListener(this)

        for (post in peer.timeline())
            addPost(post)

        scrollPane.add(logoutButton)

        scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        scrollPane.preferredSize = Dimension(800, 600)

        add(postLabel)
        add(postButton)
        add(postText)
        add(logoutButton)
        add(scrollPane)
    }

    fun addPost(post: Post) {
        val post = PostPanel(post, peer, this)
        posts.add(post)
        panel.add(post)

        val separator = JSeparator(SwingConstants.HORIZONTAL)

        separator.preferredSize = Dimension(800, 25)

        panel.add(separator)
    }


    override fun actionPerformed(event: ActionEvent) {
        when (event.source) {
            logoutButton -> {
                gui.replacePanel(Login(gui))
            }

            postButton -> {
                val post =
                    if (postText.text == "")
                        Post(UUID.randomUUID(), "Empty", peer.user)
                    else
                        Post(UUID.randomUUID(), postText.text, peer.user)

                peer.storage.addPost(post)

                addPost(post)

                gui.frame.revalidate()
            }
        }
    }

    fun updateFollowButtons(user: User, isFollow: Boolean){
        for(p in posts){
            if(p.post.author == user){
                if(isFollow){
                    p.followButton.text = "Following"
                    p.panel.revalidate()
                }
                else{
                    p.followButton.text = "Follow"
                    p.panel.revalidate()
                }
            }
        }
    }
}