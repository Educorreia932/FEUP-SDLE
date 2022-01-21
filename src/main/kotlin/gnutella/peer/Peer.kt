package gnutella.peer

import social.User
import gnutella.Constants
import gnutella.messages.*
import org.graphstream.graph.Graph
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

/**
 * Representation of a Gnutella node
 */
class Peer(
	user: User, address: String = "127.0.0.1", port: Int = freePort(),
	@Transient val graph: Graph
) : Node(user, InetAddress.getByName(address), port) {
	@Transient
	private val routingTable = RoutingTable(this, graph)

	@Transient
	private val messageBroker = MessageBroker(this)

	@Transient
	val cache = Cache(this)

	@Transient
	val sentQueryIDs = mutableSetOf<UUID>()

	@Transient
	val pinger: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

	@Transient
	val postSearcher: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

	@Transient
	var wantsToReachMaxNeighbours = true

	init {
		val node = graph.addNode(port.toString())

		node.setAttribute("ui.label", "Peer ${user.username}")
	}

	fun connect() {
		val socket = Socket(Constants.HOST_CACHE_ADDRESS, Constants.HOST_CACHE_PORT)
		var possibleNeighbours: Set<Node>

		socket.use {
			val objectOutputStream = ObjectOutputStream(socket.getOutputStream())

			objectOutputStream.use {
				objectOutputStream.writeObject(RequestConnect(UUID.randomUUID(), this))

				var response: ConnectTo
				val objectInputStream = ObjectInputStream(socket.getInputStream())

				objectInputStream.use {
					response = objectInputStream.readObject() as ConnectTo
				}

				possibleNeighbours = response.possibleNeighbours
			}
		}

		if (possibleNeighbours.isNotEmpty()) {
			// TODO: What do here?
			for (possibleNeighbour in possibleNeighbours) {
				routingTable.addNeighbour(Neighbour(possibleNeighbour as Peer), true)
				//sendMessageTo(AddNeighbour(UUID.randomUUID(), this), possibleNeighbour)
			}

//            ping()
		}


		pinger.scheduleAtFixedRate(
			{
				ping()
			},
			0,
			ThreadLocalRandom.current().nextLong(
				Constants.MIN_PING_INTERVAL.toLong(),
				Constants.MAX_PING_INTERVAL.toLong()
			),
			TimeUnit.SECONDS
		)

		// Search followers for posts every x milliseconds
		postSearcher.scheduleAtFixedRate(
			{ searchAllFollowers() },
			Constants.INITIAL_SEARCH_FOLLOWERS_TIME_MILIS.toLong(),
			Constants.SEARCH_FOLLOWERS_INTERVAL_MILLIS.toLong(),
			TimeUnit.MILLISECONDS
		)
	}

	private fun ping(TTL: Int) {
		val message = Ping(UUID.randomUUID(), this, this, TTL, 0)

		cache.addPing(message)
		routingTable.forwardMessage(message, routingTable.neighbours.take(routingTable.neighbours.size / 2).toSet())
	}

	private fun ping() {
		if ((!wantsToReachMaxNeighbours && !hasSatisfactoryNeighbours()) || (wantsToReachMaxNeighbours && !hasMaxNeighbours())) {
			ping(Constants.TTL)
		}
		else {
			ping(1)
		}
	}

	fun search(username: String) {
		val message = Query(
			UUID.randomUUID(),
			this,
			this,
			username
		)

		cache.addQuery(message)
		sentQueryIDs.add(message.ID)
		routingTable.forwardMessage(message)
	}

	fun discover(searchString: String) {
		val message = Discover(
			UUID.randomUUID(),
			this,
			this,
			searchString,
			Constants.TTL,
			0
		)
        
		user.storage.emptySearchPosts()
		routingTable.forwardMessage(message)
	}

	private fun searchAllFollowers() {
		for (f in user.following) {
			if (Constants.LOGGING)
				println("Peer " + user.username + " | Searching for follower " + f.username + ".")
			
			search(f.username)
		}
	}

	fun forwardMessage(query: Query, propagator: Node) {
		routingTable.forwardMessage(query, propagator)
	}

	fun forwardMessage(message: Message, propagator: Node) {
		routingTable.forwardMessage(message, propagator)
	}

	override fun equals(other: Any?): Boolean {
		return user.username == (other as Peer).user.username
	}

	override fun hashCode(): Int {
		return user.username.hashCode()
	}

	fun sendMessageTo(message: Message, propagator: Node) {
		messageBroker.putMessage(message.to(propagator))
	}

	fun addNeighbour(peer: Peer, notify: Boolean) {
		routingTable.addNeighbour(peer, notify)
	}

	fun removeNeighbour(neighbour: Neighbour) {
		routingTable.removeNeighbour(neighbour)
	}

	fun removeRandomNeighbour(): Neighbour {
		val neighbour = routingTable.neighbours.random()

		routingTable.removeNeighbour(neighbour)

		return neighbour
	}

	fun hasNoNeighbours(): Boolean {
		return routingTable.neighbours.isEmpty()
	}

	fun hasMaxNeighbours(): Boolean {
		return routingTable.neighbours.size == Constants.MAX_NEIGHBOURS
	}

	fun hasSatisfactoryNeighbours(): Boolean {
		return routingTable.neighbours.size >= Constants.MIN_NEIGHBOURS
	}

	fun addFriendMessage(queryHit: QueryHit, me: Peer) {
		routingTable.friends.addFriendMessage(queryHit, queryHit.digest.user.username, me)
	}

	fun stop() {
		pinger.shutdownNow()
		postSearcher.shutdownNow()
		messageBroker.stop()
//		graph.removeNode(port.toString())
	}

	companion object {
		fun freePort(): Int {
			val socket = ServerSocket(0)

			socket.reuseAddress = true

			val port = socket.localPort

			socket.close()

			return port
		}
	}
}