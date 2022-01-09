package gnutella.peer

import Digest
import Post
import User
import java.util.*

class Storage {
    val posts = mutableMapOf<User, MutableList<Post>>()

    fun addPost(post: Post) {
        if (post.author !in posts)
            posts[post.author] = mutableListOf()

        posts[post.author]?.add(post)
    }

    fun digest(user: User): Digest {
        val postIDs = mutableSetOf<UUID>()

        if (user in posts)
            for (post in posts[user]!!)
                postIDs.add(post.ID)

        return Digest(user, postIDs)
    }

    fun retrievePosts(digest: Digest): List<Post>? {
        return posts[digest.user]?.filter { it.ID in digest.postIDs }
    }

    fun timeline(user: User): List<Post> = posts.filter { it.key in user.following || it.key == user }.values.flatten()
}