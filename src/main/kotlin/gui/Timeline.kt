package gui

import Post
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

    init {
        add(logoutButton)
        
        val refreshButton = JButton("Refresh") // TODO
        val panel = JPanel()

        panel.layout = BoxLayout(panel, BoxLayout.PAGE_AXIS)
        
        logoutButton.addActionListener(this)
        
        panel.add(logoutButton)

        add(postLabel)
        add(postButton)
        add(postText)
        
        postButton.addActionListener(this)
        
        for (post in peer.timeline()) {
            panel.add(PostPanel(post))

            val separator = JSeparator(SwingConstants.HORIZONTAL)
            
            separator.preferredSize = Dimension(800, 25)

            panel.add(separator)
        }
        
        val scrollPane = JScrollPane(panel)
        
        scrollPane.add(logoutButton)

        scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        scrollPane.preferredSize = Dimension(800, 600)
        
        add(logoutButton)
        add(scrollPane)
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