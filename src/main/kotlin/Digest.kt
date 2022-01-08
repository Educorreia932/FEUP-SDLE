import java.io.Serializable
import java.util.*

data class Digest(
    val postIDs: Set<UUID>
) : Serializable {
    operator fun minus(digest: Digest): Digest = Digest(postIDs - digest.postIDs)
}