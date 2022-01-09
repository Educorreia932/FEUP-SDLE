package gui

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*

class Login(private val gui: GUI) : JPanel(), ActionListener {
    private val peers = gui.peers
    
    private var userLabel = JLabel("Username")
    private var passwordLabel = JLabel("Password")
    private var userTextField = JTextField()
    private var passwordField = JPasswordField()
    private var loginButton = JButton("Login")
    private var resetButton = JButton("Reset")
    private var showPassword = JCheckBox("Show Password")

    init {
        isVisible = true

        setLayoutManager()
        setLocationAndSize()
        addComponentsToContainer()
        addActionEvent()
    }

    private fun setLayoutManager() {
        layout = null
    }

    private fun setLocationAndSize() {
        userLabel.setBounds(50, 150, 100, 30)
        passwordLabel.setBounds(50, 220, 100, 30)
        userTextField.setBounds(150, 150, 150, 30)
        passwordField.setBounds(150, 220, 150, 30)
        showPassword.setBounds(150, 250, 150, 30)
        loginButton.setBounds(50, 300, 100, 30)
        resetButton.setBounds(200, 300, 100, 30)
    }

    private fun addComponentsToContainer() {
        add(userLabel)
        add(passwordLabel)
        add(userTextField)
        add(passwordField)
        add(showPassword)
        add(loginButton)
        add(resetButton)
    }

    private fun addActionEvent() {
        loginButton.addActionListener(this)
        resetButton.addActionListener(this)
        showPassword.addActionListener(this)
        loginButton.addActionListener(this)
    }

    override fun actionPerformed(event: ActionEvent) {
        when (event.source) {
            loginButton -> {
                val username = userTextField.text

                val peer = peers.find { it.user.username == username }

                if (peer != null)
                    gui.replacePanel(Timeline(gui, peer))
                else
                    println("No such user")
            }

            resetButton -> {
                userTextField.text = ""
                passwordField.text = ""
            }

            showPassword -> {
                if (showPassword.isSelected)
                    passwordField.echoChar = 0.toChar()
                else
                    passwordField.echoChar = '*'
            }
        }
    }
}