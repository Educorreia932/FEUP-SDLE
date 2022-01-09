package gui

import Post
import gnutella.peer.Peer
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util.*
import javax.swing.*

class Timeline(private val gui: GUI, val peer: Peer) : JPanel(), ActionListener {
    private val logoutButton = JButton("Log out")
    private val postButton = JButton("Post")
    private val postText = JTextArea(12, 24)
    private var postLabel = JLabel("Post:")

    init {
        start()
    }

    private fun start(){
        add(logoutButton)
        logoutButton.addActionListener(this)

        for (post in peer.timeline())
            add(PostPanel(post))

        add(postLabel)
        add(postButton)
        add(postText)
        postButton.addActionListener(this)
    }

    private fun startNew(){
        add(logoutButton)

        for (post in peer.timeline())
            add(PostPanel(post))

        add(postLabel)
        add(postButton)
        add(postText)
    }

    override fun actionPerformed(event: ActionEvent) {
        when (event.source) {
            logoutButton -> {
                gui.replacePanel(Login(gui))
            }
            postButton -> {
                removeAll()
                if(postText.text == "")
                    peer.storage.addPost(Post(UUID.randomUUID(), "Empty", peer.user))
                else
                    peer.storage.addPost(Post(UUID.randomUUID(), postText.text, peer.user))
                postText.text = ""
                startNew()
                gui.frame.revalidate()
            }
        }
    }
}