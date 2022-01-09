import java.io.*

class User(
    val username: String,
): Serializable {
    private var posts = ArrayList<Post>()

    init {
        // Checks if there are posts from current user to be loaded.
        loadPostsFromFile()
    }

    // Create post and save it to file.
    fun createPost(content: String) {
        val post = Post(content, author = this)
        posts.add(post)
        savePostsToFile()
    }

    // Saves all user's posts to file.
    private fun savePostsToFile() {
        try {
            val filePath = "posts/$username.txt"

            val fileOut = FileOutputStream(filePath)
            val out = ObjectOutputStream(fileOut)

            out.writeObject(this.posts)
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

            this.posts = objIn.readObject() as ArrayList<Post>

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
}