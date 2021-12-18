package activitypub

import utils.Either

open class CollectionPage: Collection() {
    lateinit var partOf: Either<Link, Collection>
    lateinit var next: Either<CollectionPage, Link>
    lateinit var prev: Either<CollectionPage, Link>
}