package gnutella

import User
import gnutella.peer.Neighbour
import gnutella.peer.Peer
import java.io.DataInputStream
import java.net.InetAddress
import java.net.Socket

import java.io.DataOutputStream
import java.lang.Exception
import java.net.SocketTimeoutException
import java.nio.charset.StandardCharsets


//val entryAddress: String = "127.0.0.1" //localhost
const val entryPort: Int = 8011

const val myConnectionAddress: String = "127.0.0.1" //localhost
const val myMessageAddress: String = "127.0.0.1" //localhost

const val myConnectionPort: Int = 9123
const val myMessagePort: Int = 9122

const val myPeerId: Int = 42

fun main(){
    val tcpSocket = Socket(InetAddress.getLocalHost(), entryPort, InetAddress.getLocalHost(), myConnectionPort)

    val dout = DataOutputStream(tcpSocket.getOutputStream())



    dout.writeUTF(Constants.CONNECTION_REQUEST_STRING + Constants.CONNECTION_MESSAGE_SEPARATOR + myMessageAddress + Constants.CONNECTION_MESSAGE_SEPARATOR + myMessagePort + Constants.CONNECTION_MESSAGE_SEPARATOR + myPeerId)
    dout.flush();
    //dout.close()


    // Read input
    try{
        // Only try to read for a certain amount of time
        tcpSocket.soTimeout = Constants.CONNECTION_TIMEOUT_MILIS
        val inputStream = DataInputStream(tcpSocket.getInputStream())
        val stringReceived = inputStream.readUTF()

        val splitStr = stringReceived.split(Constants.CONNECTION_MESSAGE_SEPARATOR)
        if(splitStr.size != 4){
            println("Connection returned an invalid number of fields. Exiting.")
            return
        }

        if(splitStr[0].equals(Constants.CONNECTION_ACCEPTANCE_STRING)){
            //Socket has to be closed before peer is created
            tcpSocket.close()

            var peer = Peer(User(myPeerId.toString()), port = myMessagePort)
            peer.addNeighbour(Neighbour(User(splitStr[3]), port = splitStr[2].toInt()))
        }
    }
    catch (exception: SocketTimeoutException){
        println("No response. Exiting.")
        tcpSocket.close()
        return
    }
    dout.close()
    return
}