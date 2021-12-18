package activitypub

import java.awt.PageAttributes
import java.net.URI

class Link {
    lateinit var href: URI
    lateinit var rel: URI
    lateinit var mediaType: PageAttributes.MediaType
    lateinit var name: String
}