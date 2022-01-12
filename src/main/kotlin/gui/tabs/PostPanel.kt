package gui.tabs

import social.Post
import java.awt.*
import java.awt.geom.RoundRectangle2D
import java.awt.image.BufferedImage
import java.net.URL
import java.util.*
import javax.imageio.ImageIO
import javax.swing.*


class PostPanel(post: Post) : JPanel() {
    init {
        val right = JPanel()
        val left = JPanel()

        left.layout = BoxLayout(left, BoxLayout.PAGE_AXIS)
        right.layout = BoxLayout(right, BoxLayout.PAGE_AXIS)

        val header = JPanel()

        header.layout = FlowLayout(FlowLayout.LEFT, 1, 0)

        // Name 
        val name = Label("Eduardo Correia")

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

        val followButton = JButton("Follow")

        // Add components
        header.add(name)
        header.add(username)

        right.add(header)
        right.add(date)
        right.add(content)

        val constraints = GridBagConstraints()

        constraints.anchor = GridBagConstraints.NORTH

        left.add(avatar)
        left.add(Box.createRigidArea(Dimension(0, 5)))
        left.add(followButton)

        add(left, constraints)
        add(right, constraints)
    }
}