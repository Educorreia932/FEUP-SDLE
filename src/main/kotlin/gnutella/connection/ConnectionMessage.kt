package gnutella.connection

import gnutella.Constants

class ConnectionMessage {

    companion object {
        fun getConnMsg(address: String, port: Int, username: String): String {
            return Constants.CONNECTION_REQUEST_STRING + Constants.CONNECTION_MESSAGE_SEPARATOR + address + Constants.CONNECTION_MESSAGE_SEPARATOR + port + Constants.CONNECTION_MESSAGE_SEPARATOR + username
        }


        fun getConnAcceptMsg(address: String, port: Int, username: String): String {
            return Constants.CONNECTION_ACCEPTANCE_STRING + Constants.CONNECTION_MESSAGE_SEPARATOR + address + Constants.CONNECTION_MESSAGE_SEPARATOR + port + Constants.CONNECTION_MESSAGE_SEPARATOR + username
        }
    }
}
