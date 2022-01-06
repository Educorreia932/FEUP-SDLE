package gnutella.peer

import User
import gnutella.*
import gnutella.connection.ConnectionMessage
import gnutella.messages.Message
import gnutella.messages.Ping
import gnutella.messages.Query
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.InetAddress
import java.net.Socket
import java.net.SocketTimeoutException
import java.util.*

/**
 * Representation of a Gnutella node
 */
class Peer(
    val user: User,
    val address: String = "127.0.0.1",
    val port: Int,
) {
    private val neighbours = mutableSetOf<Neighbour>()
    private lateinit var messageBroker: MessageBroker
    val cache = Cache(this)
    val storage = Storage()

    constructor(user: User, address: String, port: Int, myConnectionAddress: String, myConnectionPort: Int) : this(user, address, port) {
        messageBroker = MessageBroker(this, myConnectionAddress, myConnectionPort)
    }

    constructor(user: User, address: String, port: Int, myConnectionAddress: String, myConnectionPort: Int, addressaddressToConnect: String, portToConnect: Int) : this(user, address, port) {
        messageBroker = MessageBroker(this)
        val tcpSocket = Socket(InetAddress.getLocalHost(), portToConnect, InetAddress.getLocalHost(), myConnectionPort)

        val dout = DataOutputStream(tcpSocket.getOutputStream())


        dout.writeUTF(ConnectionMessage.getConnMsg(address, port, user.username))
        dout.flush()


        // Read input
        try {
            // Only try to read for a certain amount of time
            tcpSocket.soTimeout = Constants.CONNECTION_TIMEOUT_MILIS
            val inputStream = DataInputStream(tcpSocket.getInputStream())
            val stringReceived = inputStream.readUTF()

            val splitStr = stringReceived.split(Constants.CONNECTION_MESSAGE_SEPARATOR)
            if (splitStr.size != 4) {
                println("Connection returned an invalid number of fields. Exiting.")
                return
            }

            if (splitStr[0].equals(Constants.CONNECTION_ACCEPTANCE_STRING)) {
                //Socket has to be closed before peer is created
                tcpSocket.close()

                var peer = Peer(User(user.username), port = myConnectionPort)
                peer.addNeighbour(Neighbour(User(splitStr[3]), port = splitStr[2].toInt()))
            }
        } catch (exception: SocketTimeoutException) {
            println("No response. Exiting.")
            tcpSocket.close()
            return
        }
        dout.close()

        messageBroker.createConnectionAcceptSocket(myConnectionAddress, myConnectionPort)
    }

    fun addNeighbour(address: String, port: Int) {
        addNeighbour(Neighbour(User(""), address, port))
    }

    fun addNeighbour(neighbour: Neighbour) {
        if ((!neighbour.sameAsPeer(this)) && neighbour !in neighbours)
            neighbours.add(neighbour)
    }

    fun addNeighbour(peer: Peer) {
        val neighbour = Neighbour(peer)
        addNeighbour(neighbour)
    }

    // Test function; Prints all neighbours' info
    fun printNeighbours() {
        println("Neighbours: ")
        for (i in neighbours) {
            println("Username: ${i.user.username}; Address: ${i.address}; Port: ${i.port}")
        }
    }

    fun removeNeighbour(address: String, port: Int) {
        neighbours.remove(Neighbour(User(""), address, port))
    }

    fun removeNeighbour(neighbour: Neighbour) {
        if ((!neighbour.sameAsPeer(this)) && neighbour in neighbours)
            neighbours.remove(neighbour)
    }

    fun ping() {
        val message = Ping(UUID.randomUUID(), Constants.MAX_HOPS, 0)

        forwardMessage(message, null)
    }

    fun search(keyword: String) {
        val message = Query(UUID.randomUUID(), Constants.MAX_HOPS, 0, keyword)

        forwardMessage(message, null)
    }

    fun sendMessage(message: Message, address: String, port: Int) {
        val msg = message.to(address, port)
        messageBroker.putMessage(message.to(address, port))
    }

    fun sendMessage(message: Message, neighbour: Neighbour) {
        val msg = message.to(neighbour)
        messageBroker.putMessage(msg)
    }

    fun forwardMessage(message: Message, neighbourToExclude: Neighbour?) {
        for (neighbour in neighbours)
            if (neighbour != neighbourToExclude)
                sendMessage(message, neighbour)
    }

    override fun equals(other: Any?): Boolean {
        return user.username == (other as Peer).user.username
    }

    override fun hashCode(): Int {
        return user.username.hashCode()
        // return  31 * result + neighbours.hashCode()
    }
}