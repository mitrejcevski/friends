package nl.jovmit.friends.domain.user

import nl.jovmit.friends.domain.exceptions.DuplicateAccountException

class InMemoryUserCatalog(
  private val usersForPassword: MutableMap<String, MutableList<User>> = mutableMapOf()
) : UserCatalog {

  override suspend fun createUser(
    email: String,
    password: String,
    about: String
  ): User {
    checkAccountExists(email)
    val userId = createUserIdFor(email)
    val user = User(userId, email, about)
    saveUser(password, user)
    return user
  }

  fun followedBy(userId: String): List<String> {
    val followings = listOf(
      Following("saraId", "lucyId"),
      Following("annaId", "lucyId")
    )
    return followings
      .filter { it.userId == userId }
      .map { it.followedId }
  }

  private fun checkAccountExists(email: String) {
    if (usersForPassword.values.flatten().any { it.email == email }) {
      throw DuplicateAccountException()
    }
  }

  private fun createUserIdFor(email: String): String {
    return email.takeWhile { it != '@' } + "Id"
  }

  private fun saveUser(password: String, user: User) {
    usersForPassword.getOrPut(password, ::mutableListOf).add(user)
  }
}