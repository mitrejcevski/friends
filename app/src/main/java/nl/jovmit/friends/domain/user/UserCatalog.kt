package nl.jovmit.friends.domain.user

interface UserCatalog {

  suspend fun createUser(
    email: String,
    password: String,
    about: String
  ): User

  suspend fun followedBy(userId: String): List<String>

  suspend fun loadFriendsFor(userId: String): List<Friend>
}