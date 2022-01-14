package social

import gnutella.social.Storage
import java.io.*
import java.util.*

class User(
    val username: String,
    @Transient
    val following: MutableSet<User> = mutableSetOf(),
) : Serializable {
    @Transient
    val storage: Storage = Storage()

    init {
        // Checks if there are posts from current user to be loaded.
        loadPostsFromFile()
    }

    // Create post and save it to file.
    fun createPost(content: String) {
        val post = Post(UUID.randomUUID(), content, author = this)

        storage.addPost(post)
        savePostsToFile()
    }

    // Deletes all user's posts.
    fun clearPosts() {
        storage.posts.clear()
        
        savePostsToFile()
    }

    // Saves all user's posts to file.
    private fun savePostsToFile() {
        try {
            val filePath = "posts/$username.txt"

            val fileOut = FileOutputStream(filePath)
            val out = ObjectOutputStream(fileOut)

            out.writeObject(this.storage.posts)
            out.close()
            fileOut.close()
            println("Serialized data is saved")
        } catch (i: IOException) {
            i.printStackTrace()
        }
    }

    // Loads all user's posts from file.
    private fun loadPostsFromFile() {
        try {
            val filePath = "posts/$username.txt"
            val file = File(filePath)

            if (!file.isFile) {
                return
            }

            val fileIn = FileInputStream(filePath)
            val objIn = ObjectInputStream(fileIn)

//            storage.posts = objIn.readObject() as ArrayList<Post>

            objIn.close()
            fileIn.close()
        } catch (i: IOException) {
            i.printStackTrace()
            return
        } catch (c: ClassNotFoundException) {
            println("Class not found")
            c.printStackTrace()
            return
        }
    }


    fun isFollowing(user: User): Boolean {
        return user in following
    }

    fun follow(user: User) {
        if (user != this)
            following.add(user)
    }

    fun unfollow(user: User) {
        following.remove(user)
    }

    fun timeline(): List<Post> {
        return storage.timeline(this)
    }

    override fun toString(): String {
        return "social.User $username"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true

        if (javaClass != other?.javaClass)
            return false

        other as User

        return username == other.username
    }

    override fun hashCode(): Int {
        return username.hashCode()
    }
}