package nl.jovmit.friends.domain.user

import nl.jovmit.friends.domain.exceptions.DuplicateAccountException

class InMemoryUserCatalog(
  private val emailsToUsers: MutableMap<String, User> = mutableMapOf(),
  private val followings: List<Following> = mutableListOf()
) : UserCatalog {

  override suspend fun createUser(
    email: String,
    password: String,
    about: String
  ): User {
    checkAccountExists(email)
    val userId = createUserIdFor(email)
    val user = User(userId, email, about)
    saveUser(user)
    return user
  }

  override fun followedBy(userId: String): List<String> {
    return followings
      .filter { it.userId == userId }
      .map { it.followedId }
  }

  private fun checkAccountExists(email: String) {
    if (emailsToUsers.containsKey(email)) {
      throw DuplicateAccountException()
    }
  }

  private fun createUserIdFor(email: String): String {
    return email.takeWhile { it != '@' } + "Id"
  }

  private fun saveUser(user: User) {
    emailsToUsers[user.email] = user
  }
}