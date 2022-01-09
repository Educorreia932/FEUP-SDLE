package gui

import java.awt.EventQueue


private fun createAndShowGUI() {
    val frame = Login()

    frame.isVisible = true
}

fun main() {
    EventQueue.invokeLater(::createAndShowGUI)
}