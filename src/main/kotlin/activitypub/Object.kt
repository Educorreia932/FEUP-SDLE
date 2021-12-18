package activitypub

import utils.Either
import java.net.URI

open class Object {
    lateinit var attachment: Either<Object, Link>
    lateinit var attributedTo: Either<Object, Link>
    lateinit var audience: Either<Object, Link>
    lateinit var content: Either<Object, Link>
    lateinit var context: Either<Object, Link>
    lateinit var name: String
    
}