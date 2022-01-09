package gui

import java.awt.Container
import javax.swing.JFrame

class Timeline: JFrame() {
    private val container: Container = contentPane

    init {
        title = "Tulicreme | Timeline"
        isVisible = true
        defaultCloseOperation = EXIT_ON_CLOSE
        isResizable = false

        setBounds(10, 10, 370, 600)
        
        container.add(PostPanel())
    }
}