package gui.components

import social.Post
import social.User
import java.awt.*
import java.net.URL
import java.util.*
import javax.imageio.ImageIO
import javax.swing.*

class PostPanel(user: User, post: Post, postList: PostList) : JPanel() {
    private val followButton = JButton()

    init {
        val right = JPanel()
        val left = JPanel()

        left.layout = BoxLayout(left, BoxLayout.PAGE_AXIS)
        right.layout = BoxLayout(right, BoxLayout.PAGE_AXIS)

        val header = JPanel()

        header.layout = FlowLayout(FlowLayout.LEFT, 1, 0)

        // Name 
        val name = Label("${post.author.name}")

        name.font = font.deriveFont(font.style or Font.BOLD)

        // Username
        val username = Label("@${post.author.username}")

        username.foreground = Color.decode("#777777")

        // Content
        val content = JTextArea(post.content)

        content.isEditable = false

        // Date
        val date = Label(Date().toString())

        date.foreground = Color.decode("#777777")

        // Avatar 
        val path = "https://avatars.dicebear.com/api/initials/${post.author.username}.png"
        val imageIcon = ImageIcon(ImageIO.read(URL(path)).getScaledInstance(75, 75, Image.SCALE_DEFAULT))
        val avatar = JLabel(imageIcon)

        avatar.preferredSize = Dimension(75, 75)

        // Follow button 
        val followText = if (user.isFollowing(post.author)) "Following" else "Follow"
        followButton.text = followText

        followButton.addActionListener {
            if (user.isFollowing(post.author))
                user.unfollow(post.author)
            
            else
                user.follow(post.author)

            for (postPanel in postList.postPanels)
                postPanel.followButton.text = if (user.isFollowing(post.author)) "Following" else "Follow"
        }

        // Add components
        header.add(name)
        header.add(username)

        right.add(header)
        right.add(date)
        right.add(content)

        val constraints = GridBagConstraints()

        constraints.anchor = GridBagConstraints.NORTH

        left.add(avatar)
        left.add(Box.createRigidArea(Dimension(0, 5))) // Vertical spacing

        if (user != post.author)
            left.add(followButton)

        add(left, constraints)
        add(right, constraints)
    }
}