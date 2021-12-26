package gnutella.peer

import Post

class Storage {
    private val posts = mutableListOf<Post>()

    fun addPost(post: Post) {
        posts.add(post)
    }

    fun retrievePosts(username: String): List<Post> {
        return posts.filter { post -> post.author.username == username }
    }
}