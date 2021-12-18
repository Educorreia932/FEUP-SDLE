package activitypub

import utils.Either

open class Collection: Object() {
    lateinit var current: Either<CollectionPage, Link>
    lateinit var first: Either<CollectionPage, Link>
    lateinit var last: Either<CollectionPage, Link>
    lateinit var items: List<Either<Object, Link>>
}