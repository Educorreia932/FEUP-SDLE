package gui.frames

import gnutella.peer.Peer
import org.graphstream.graph.Graph
import social.User
import java.awt.Image
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.imageio.ImageIO
import javax.swing.*


class Login(val peers: MutableList<Peer>, private val graph: Graph) : JFrame(), ActionListener {
	private var userLabel = JLabel("Username")
	private var userTextField = JTextField()
	private var loginButton = JButton("Login")
	private var resetButton = JButton("Reset")

	init {
		title = "Tulicreme | Login"
		isVisible = true
		defaultCloseOperation = EXIT_ON_CLOSE
		layout = null

		setBounds(10, 10, 800, 600)
		setLocationAndSize()
		addComponentsToContainer()
		addActionEvent()
	}

	private fun setLocationAndSize() {
		val bannerImage = ImageIO.read(Login::class.java.getResource("/banner.png"))
			.getScaledInstance(200, 100, Image.SCALE_DEFAULT)
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
				var peer = peers.find { it.user.username == username }

				if (peer == null) {
					peer = Peer(User(username), graph = graph)
					
					peers.add(peer)
				}

				Application(peer)
			}

			resetButton -> {
				userTextField.text = ""
			}
		}
	}
}