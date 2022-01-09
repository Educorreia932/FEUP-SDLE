import java.io.Serializable
import java.util.*

data class Post(
    val ID: UUID,
    val content: String,
    val author: User,
    val date: Date = Date()
) : Serializable {
    override fun toString(): String {
        return "$content, $author, $date"
    }
}