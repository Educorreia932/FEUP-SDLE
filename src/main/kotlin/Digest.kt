import java.io.Serializable
import java.util.*

data class Digest(
    val user: User,
    val postIDs: Set<UUID>
) : Serializable {
    operator fun minus(digest: Digest): Digest = Digest(user, postIDs - digest.postIDs)
}