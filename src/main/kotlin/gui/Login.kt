package gui

import java.awt.Container
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*
import javax.swing.text.AbstractDocument
import javax.swing.text.AttributeSet
import javax.swing.text.BadLocationException
import javax.swing.text.DocumentFilter


class Login : JFrame(), ActionListener {
    private class UsernameFilter : DocumentFilter() {
        override fun insertString(
            fb: FilterBypass?,
            offset: Int,
            string: String?,
            attr: AttributeSet?
        ) {
            if (offset >= 1) {
                super.insertString(fb, offset, string, attr)
            }
        }

        override fun remove(fb: FilterBypass, offset: Int, length: Int) {
            if (offset >= 1) {
                super.remove(fb, offset, length)
            }
        }

        @kotlin.Throws(BadLocationException::class)
        override fun replace(
            fb: FilterBypass?,
            offset: Int,
            length: Int,
            text: String?,
            attrs: AttributeSet?
        ) {
            if (offset >= 1) {
                super.replace(fb, offset, length, text, attrs)
            }
        }
    }

    private val container: Container = contentPane
    private var userLabel = JLabel("Username")
    private var passwordLabel = JLabel("Password")
    private var userTextField = JTextField("@")
    private var passwordField = JPasswordField()
    private var loginButton = JButton("Login")
    private var resetButton = JButton("Reset")
    private var showPassword = JCheckBox("Show Password")

    init {
        title = "Tulicreme | Login"
        isVisible = true
        defaultCloseOperation = EXIT_ON_CLOSE
        isResizable = false

        setBounds(10, 10, 370, 600)
        setLayoutManager()
        setLocationAndSize()
        addComponentsToContainer()
        addActionEvent()
    }

    private fun setLayoutManager() {
        container.layout = null
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
        (userTextField.document as AbstractDocument).documentFilter = UsernameFilter()

        container.add(userLabel)
        container.add(passwordLabel)
        container.add(userTextField)
        container.add(passwordField)
        container.add(showPassword)
        container.add(loginButton)
        container.add(resetButton)
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

                val timeline = Timeline()

                timeline.isVisible = true

                dispose()
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