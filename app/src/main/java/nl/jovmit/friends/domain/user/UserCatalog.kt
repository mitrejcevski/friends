package nl.jovmit.friends.domain.user

interface UserCatalog {

  suspend fun createUser(
    email: String,
    password: String,
    about: String
  ): User

  suspend fun followedBy(userId: String): List<String>

  suspend fun loadPeopleFor(userId: String): List<Friend>
}