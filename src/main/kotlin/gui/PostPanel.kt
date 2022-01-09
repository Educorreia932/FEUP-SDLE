package gui

import java.awt.Font
import java.awt.Label
import javax.swing.JPanel

class PostPanel : JPanel() {
    init {
        val name = Label("Eduardo Correia")
        val username = Label("@educorreia932")
        val content = Label("This is my first post")
        val date = Label("Just now")

        val font = Font("Courier", Font.BOLD, 12)
        name.font = font.deriveFont(font.style or Font.BOLD)
        
        add(name)
        add(username)
        add(date)
        add(content)
        
    }
}