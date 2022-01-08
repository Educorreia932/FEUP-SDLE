import java.io.Serializable
import java.util.*

data class Post(
    val content: String,
    val author: User,
    val date: Date
): Serializable