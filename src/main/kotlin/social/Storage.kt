package gnutella.social

import social.Digest
import social.Post
import social.User
import java.util.*

class Storage {
    var posts = mutableMapOf<User, MutableList<Post>>()
    private val searchPosts = mutableListOf<Post>()

    fun replaceSearchPosts(postList: List<Post>) {
        searchPosts.removeAll(searchPosts)

        println("Received posts: ")

        for (p in postList) {
            searchPosts.add(p)

            println("Post: $p")
        }
    }

    fun getSearchPosts(): MutableList<Post> {
        return searchPosts
    }

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

    fun timeline(user: User): List<Post> =
        posts.filter { it.key in user.following || it.key == user }
            .values
            .flatten()
            .sortedByDescending { it.date }

    fun findMatchingPosts(keywordString: String): MutableList<Post> {
        val keywords = keywordString.split(" ")
        val results = mutableListOf<Post>()
        
        for (u in posts.values) {
            for (p in u) {
                val list = p.content.split(" ")
                
                for (w in list) {
                    if (w in keywords) {
                        results.add(p)
                        
                        break
                    }
                }
            }
        }
        
        return results
    }
}