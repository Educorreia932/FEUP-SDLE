package gui

import Post
import java.awt.*
import java.net.URL
import java.util.*
import javax.imageio.ImageIO
import javax.swing.*


class PostPanel(post: Post) : JPanel() {
    init {
        layout = GridBagLayout()

        val right = JPanel()
        val left = JPanel()

        left.layout = BoxLayout(left, BoxLayout.PAGE_AXIS)
        right.layout = BoxLayout(right, BoxLayout.PAGE_AXIS)

        val panel = JPanel()

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
        val imageIcon = ImageIcon(ImageIO.read(URL(path)).getScaledInstance(75, 75, Image.SCALE_DEFAULT))
        val avatar = JLabel(imageIcon)

        avatar.preferredSize = Dimension(75, 75)

        val followButton = JButton("Follow")

        // Add components
        panel.add(name)
        panel.add(username)
        panel.add(date)

        right.add(panel)
        right.add(content)

        val constraints = GridBagConstraints()

        constraints.anchor = GridBagConstraints.PAGE_START

        left.add(avatar)
        left.add(followButton)
        
        add(left, constraints)
        add(right, constraints)
    }
}