package social

import java.util.*

class Storage {
	val posts = mutableMapOf<User, MutableSet<Post>>()
	val searchPosts = mutableSetOf<Post>()

	fun emptySearchPosts() {
		searchPosts.clear()
	}

	fun addSearchPosts(postList: List<Post>) {
		for (p in postList)
			searchPosts.add(p)
	}

	fun addPost(post: Post) {
		if (post.author !in posts.keys)
			posts[post.author] = mutableSetOf()

		posts[post.author]?.add(post)
	}

	fun addPosts(postList: List<Post>) {
		for (post in postList)
			addPost(post)
	}

	fun digest(user: User): Digest {
		val postIDs = mutableSetOf<UUID>()

		if (user in posts)
			for (post in posts[user]!!)
				postIDs.add(post.ID)

		return Digest(user, postIDs)
	}

	fun retrievePosts(digest: Digest): List<Post>? = posts[digest.user]?.filter { it.ID in digest.postIDs }

	fun timeline(user: User): List<Post> =
		posts.filter { it.key in user.following || it.key == user }
			.values
			.flatten()
			.sortedByDescending { it.date }

	fun findMatchingPosts(keyword: String): List<Post> = posts
		.values
		.flatten()
		.filter { keyword in it.content }
}