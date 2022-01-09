package gui

import com.formdev.flatlaf.FlatLightLaf
import java.awt.EventQueue

private fun createAndShowGUI() {
    val frame = Login()

    frame.isVisible = true
}

fun main() {
    FlatLightLaf.setup();
    
    EventQueue.invokeLater(::createAndShowGUI)
}