package gnutella.peer

import gnutella.messages.QueryHit
import kotlin.math.min

class FriendList {
    private val friendList: MutableSet<Friend> = mutableSetOf()


    fun getBestFriendsForTopic(numFriends: Int, topic: String): List<Friend> {
        val friends = friendList.sortedByDescending { it.getInterestInUser(topic) }
        var numberFriends = min(friends.size, numFriends)
        for (i in 0 until (min(friends.size, numFriends))) {
            if (friends[i].getInterestInUser(topic) == 0f) {
                numberFriends = i - 1
                break
            }
        }
        if (friends.isEmpty()) {
            numberFriends = 0
        }

        if (numberFriends == -1) {
            return listOf()
        }
        return friends.subList(0, numberFriends)
    }

    fun getBestFriendsForTopicExcept(numFriends: Int, topic: String, excluded: Node): List<Friend> {
        // Exclude previous propagator
        val fList = mutableSetOf<Friend>()
        for (i in friendList)
            if (i.user != excluded.user)
                fList.add(i)
        // Get best friends for this topic
        val friends = fList.sortedByDescending { it.getInterestInUser(topic) }

        var numberFriends = min(friends.size, numFriends)
        for (i in 0 until (min(friends.size, numFriends))) {
            if (friends[i].getInterestInUser(topic) == 0f) {
                numberFriends = i - 1
                break
            }
        }
        if (friends.isEmpty()) {
            numberFriends = 0
        }

        if (numberFriends == -1) {
            return listOf()
        }
        return friends.subList(0, numberFriends)
    }

    fun addFriendMessage(queryHit: QueryHit, topic: String, me: Peer) {
        var alreadyHasFriend = false
        for (i in friendList) {
            if (i.user.username == queryHit.source.user.username) {
                alreadyHasFriend = true
                println("Peer " + me.user.username + " | updated " + queryHit.source.user.username + "'s interest in topic " + topic)
                i.addTopicInterest(topic)
                break
            }
        }
        if (!alreadyHasFriend) {
            println("Peer " + me.user.username + " | added " + queryHit.source.user.username + " as a friend in topic " + topic)
            val friend = Friend.create(queryHit.source)
            friend.addTopicInterest(topic)
            friendList.add(friend)
        }
    }

}