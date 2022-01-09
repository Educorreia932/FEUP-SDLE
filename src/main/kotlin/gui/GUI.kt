package gui

import java.awt.Container
import java.awt.EventQueue
import javax.swing.*


class GUI : JFrame() {
    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(800, 600)
        setLocationRelativeTo(null)

        val pane = getRootPane()

        pane.layout = BoxLayout(pane, BoxLayout.Y_AXIS)

        addButton("Button 1", pane);
        addButton("Button 2", pane);
        addButton("Button 3", pane);
        addButton("Long-Named Button 4", pane);
        addButton("5", pane);
    }

    private fun addButton(text: String, container: Container) {
        val button = JButton(text)
        
        button.alignmentX = CENTER_ALIGNMENT
        container.add(button)
    }
}

private fun createAndShowGUI() {
    val frame = GUI()

    frame.isVisible = true
}

fun main() {
    EventQueue.invokeLater(::createAndShowGUI)
}