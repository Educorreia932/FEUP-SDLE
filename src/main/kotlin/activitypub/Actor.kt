package activitypub

import java.net.URI

class Actor : Object() {
    lateinit var username: String
    lateinit var inbox: URI
    lateinit var outbox: URI
    lateinit var following: URI
    lateinit var followers: URI
}