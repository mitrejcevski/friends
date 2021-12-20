package nl.jovmit.friends.domain.people

import nl.jovmit.friends.domain.exceptions.BackendException
import nl.jovmit.friends.domain.exceptions.ConnectionUnavailableException
import nl.jovmit.friends.domain.user.Friend
import nl.jovmit.friends.domain.user.User

class InMemoryPeopleCatalog {

  private val tom = Friend(User("tomId", "", ""), isFollowee = false)
  private val anna = Friend(User("annaId", "", ""), isFollowee = true)
  private val sara = Friend(User("saraId", "", ""), isFollowee = false)
  private val peopleForUserId = mapOf(
    "annaId" to listOf(tom),
    "lucyId" to listOf(anna, sara, tom),
    "saraId" to emptyList()
  )

  fun loadPeopleFor(userId: String): List<Friend> {
    if (userId.isBlank()) throw ConnectionUnavailableException()
    return peopleForUserId[userId] ?: throw BackendException()
  }
}