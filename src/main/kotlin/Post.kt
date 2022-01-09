import java.time.Instant
import java.time.format.DateTimeFormatter
import java.io.Serializable

data class Post(
    val content: String,
    val date: String? = DateTimeFormatter.ISO_INSTANT.format(Instant.now()),
    val author: User
) : Serializable