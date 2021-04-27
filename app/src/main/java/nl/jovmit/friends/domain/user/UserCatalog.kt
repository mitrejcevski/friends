package nl.jovmit.friends.domain.user

interface UserCatalog {

  suspend fun createUser(
    email: String,
    password: String,
    about: String
  ): User
}