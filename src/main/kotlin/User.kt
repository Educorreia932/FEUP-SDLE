import java.io.Serializable

data class User(
    val username: String,
    @Transient
    val following: MutableSet<User> = mutableSetOf()
) : Serializable {
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

    override fun toString(): String {
        return "User $username"
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