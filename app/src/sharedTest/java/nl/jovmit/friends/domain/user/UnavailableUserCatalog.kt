package nl.jovmit.friends.domain.user

import nl.jovmit.friends.domain.exceptions.BackendException

class UnavailableUserCatalog : UserCatalog {

  override suspend fun createUser(email: String, password: String, about: String): User {
    throw BackendException()
  }

  override fun followedBy(userId: String): List<String> {
    throw BackendException()
  }
}