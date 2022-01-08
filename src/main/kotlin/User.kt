import java.io.Serializable

data class User(
    val username: String,
    val following: MutableSet<User> = mutableSetOf()
) : Serializable {
    fun isFollowing(user: User): Boolean {
        return user in following
    }

    fun follow(user: User) {
        following.add(user)
    }

    fun unfollow(user: User){
        following.remove(user)
    }
}