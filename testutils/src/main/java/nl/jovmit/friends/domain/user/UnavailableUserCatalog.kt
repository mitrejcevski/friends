package nl.jovmit.friends.domain.user

import nl.jovmit.friends.domain.exceptions.BackendException
import nl.jovmit.friends.domain.friends.ToggleFollowing

class UnavailableUserCatalog : UserCatalog {

  override suspend fun createUser(email: String, password: String, about: String): User {
    throw BackendException()
  }

  override suspend fun toggleFollowing(userId: String, followeeId: String): ToggleFollowing {
    throw BackendException()
  }

  override suspend fun followedBy(userId: String): List<String> {
    throw BackendException()
  }

  override suspend fun loadFriendsFor(userId: String): List<Friend> {
    throw BackendException()
  }
}