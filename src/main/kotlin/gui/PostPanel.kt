package gui

import Post
import java.awt.Font
import java.awt.Label
import javax.swing.JPanel

class PostPanel(val post: Post) : JPanel() {
    init {
        val name = Label("Eduardo Correia")
        val username = Label(post.author.username)
        val content = Label(post.content)
        val date = Label(post.date.toString())

        val font = Font("Courier", Font.BOLD, 12)
        name.font = font.deriveFont(font.style or Font.BOLD)

        add(name)
        add(username)
        add(date)
        add(content)
    }
}