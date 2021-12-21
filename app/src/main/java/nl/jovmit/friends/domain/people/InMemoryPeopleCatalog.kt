package nl.jovmit.friends.domain.people

import nl.jovmit.friends.domain.exceptions.BackendException
import nl.jovmit.friends.domain.exceptions.ConnectionUnavailableException
import nl.jovmit.friends.domain.user.Friend

class InMemoryPeopleCatalog(
  private val peopleForUserId: Map<String, List<Friend>>
) : PeopleCatalog {

  override suspend fun loadPeopleFor(userId: String): List<Friend> {
    if (userId.isBlank()) throw ConnectionUnavailableException()
    return peopleForUserId[userId] ?: throw BackendException()
  }
}