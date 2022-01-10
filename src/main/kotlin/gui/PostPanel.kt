package gui

import Post
import gnutella.peer.Peer
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.net.URL
import java.util.*
import javax.imageio.ImageIO
import javax.swing.*


class PostPanel(val post: Post, val peer: Peer, val timeline: Timeline) : JPanel(), ActionListener {
    val followButton = JButton("")
    val panel = JPanel()

    init {
        layout = GridBagLayout()

        val container = JPanel()

        container.layout = BoxLayout(container, BoxLayout.PAGE_AXIS)


        // Name 
        val name = Label("Eduardo Correia")
        val font = Font("Courier", Font.BOLD, 12)

        name.font = font.deriveFont(font.style or Font.BOLD)

        // Username
        val username = Label(post.author.username)

        // Content
        val content = JTextArea(post.content)

        content.isEditable = false

        // Date
        val date = Label(Date().toString())

        // Avatar 
        val path = "https://avatars.dicebear.com/api/initials/${username.text}.png"
        val imageIcon = ImageIcon(ImageIO.read(URL(path)).getScaledInstance(100, 100, Image.SCALE_DEFAULT))
        val avatar = JLabel(imageIcon)

        avatar.preferredSize = Dimension(100, 100)

        // Add components
        panel.add(name)
        panel.add(username)
        panel.add(date)
        if (peer.user != post.author){
            followButton.addActionListener(this)
            if(peer.user.isFollowing(post.author)){
                followButton.text = "Following"
                panel.add(followButton)
            }
            else{
                followButton.text = "Follow"
                panel.add(followButton)
            }
        }

        container.add(panel)
        container.add(content)

        val constraints = GridBagConstraints()

        constraints.anchor = GridBagConstraints.PAGE_START

        add(avatar, constraints)
        add(container)
    }

    override fun actionPerformed(event: ActionEvent) {
        when (event.source) {
            followButton -> {
                if (peer.user != post.author){
                    if(peer.user.isFollowing(post.author)){
                        followButton.text = "Follow"
                        peer.user.unfollow(post.author)
                        panel.revalidate()
                        timeline.updateFollowButtons(post.author, false)
                    }
                    else{
                        followButton.text = "Following"
                        peer.user.follow(post.author)
                        panel.revalidate()
                        timeline.updateFollowButtons(post.author, true)
                    }
                }
            }
        }
    }
}