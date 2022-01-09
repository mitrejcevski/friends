package nl.jovmit.friends.domain.user

import nl.jovmit.friends.domain.exceptions.BackendException

class UnavailableUserCatalog : UserCatalog {

  override suspend fun createUser(email: String, password: String, about: String): User {
    throw BackendException()
  }

  override suspend fun followedBy(userId: String): List<String> {
    throw BackendException()
  }

  override suspend fun loadPeopleFor(userId: String): List<Friend> {
    throw BackendException()
  }
}