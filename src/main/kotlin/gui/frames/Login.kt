package gui.frames

import java.awt.Image
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.imageio.ImageIO
import javax.swing.*


class Login() : JFrame(), ActionListener {
	private var userLabel = JLabel("Username")
	private var userTextField = JTextField()
	private var loginButton = JButton("Login")
	private var resetButton = JButton("Reset")

	init {
		title = "Tulicreme | Login"
		isVisible = true
		setBounds(10, 10, 800, 600);

		setLayoutManager()
		setLocationAndSize()
		addComponentsToContainer()
		addActionEvent()
	}

	private fun setLayoutManager() {
		layout = null
	}

	private fun setLocationAndSize() {
		val bannerImage = ImageIO.read(Login::class.java.getResource("/banner.png"))
			.getScaledInstance(200, 100, Image.SCALE_DEFAULT);
		val bannerLabel = JLabel(ImageIcon(bannerImage))

		bannerLabel.setBounds(300, 100, 200, 100)
		userLabel.setBounds(250, 200, 100, 30)
		userTextField.setBounds(350, 200, 200, 30)
		loginButton.setBounds(275, 350, 100, 30)
		resetButton.setBounds(425, 350, 100, 30)

		add(bannerLabel)
	}

	private fun addComponentsToContainer() {
		add(userLabel)
		add(userTextField)
		add(loginButton)
		add(resetButton)
	}

	private fun addActionEvent() {
		resetButton.addActionListener(this)
		loginButton.addActionListener(this)
	}

	override fun actionPerformed(event: ActionEvent) {
		when (event.source) {
			loginButton -> {
				val username = userTextField.text
			}

			resetButton -> {
				userTextField.text = ""
			}
		}
	}
}