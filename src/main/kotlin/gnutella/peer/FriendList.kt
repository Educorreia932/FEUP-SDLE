package gnutella.peer

import gnutella.messages.QueryHit
import kotlin.math.min

class FriendList {
    private val friendList: MutableMap<String, MutableList<Friend>> = mutableMapOf()

    fun getBestFriendsForTopic(numFriends: Int, topic: String): List<Friend> {
        if (friendList[topic] == null) {
            return listOf()
        }
        val friends = friendList[topic]!!.sortedByDescending { it.score }
        var numberFriends = min(friends.size, numFriends)
        for (i in 0 until (min(friends.size, numFriends))) {
            if (friends[i].score == 0) {
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
        if (friendList[topic] == null) {
            return listOf()
        }
        // Get best friends for this topic
        val friends = friendList[topic]!!.sortedByDescending { it.score }

        // Exclude previous propagator
        val fList = mutableSetOf<Friend>()
        for (i in friends) {
            if (i.user != excluded.user) {
                fList.add(i)
            }
        }

        var numberFriends = min(friends.size, numFriends)
        for (i in 0 until (min(friends.size, numFriends))) {
            if (friends[i].score == 0) {
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
        if (friendList[topic] != null) {
            val auxFriend = Friend.create(queryHit.source)
            if (friendList[topic]!!.contains(auxFriend)) {
                val index = friendList[topic]!!.indexOf(auxFriend)
                friendList[topic]!![index].score++
            } else {
                friendList[topic]!!.add(auxFriend)
            }
        } else {
            friendList[topic] = mutableListOf()
            val auxFriend = Friend.create(queryHit.source)
            friendList[topic]!!.add(auxFriend)
        }
    }

}