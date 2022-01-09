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

        val container = JPanel()

        container.layout = BoxLayout(container, BoxLayout.PAGE_AXIS)

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
        val imageIcon = ImageIcon(ImageIO.read(URL(path)).getScaledInstance(100, 100, Image.SCALE_DEFAULT))
        val avatar = JLabel(imageIcon)

        avatar.preferredSize = Dimension(100, 100)

        // Add components
        panel.add(name)
        panel.add(username)
        panel.add(date)

        container.add(panel)
        container.add(content)

        val constraints = GridBagConstraints()

        constraints.anchor = GridBagConstraints.PAGE_START

        add(avatar, constraints)
        add(container)
    }
}