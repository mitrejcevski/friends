package nl.jovmit.friends.domain.user

import nl.jovmit.friends.domain.exceptions.DuplicateAccountException

class InMemoryUserCatalog(
  private val usersForPassword: MutableMap<String, MutableList<User>> = mutableMapOf(),
  private val followings: List<Following> = mutableListOf()
) : UserCatalog {

  private val allUsers: List<User>
    get() = usersForPassword.values.flatten()

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

  override suspend fun followedBy(userId: String): List<String> {
    return followings
      .filter { it.userId == userId }
      .map { it.followedId }
  }

  override suspend fun loadPeopleFor(userId: String): List<Friend> {
    val peopleFollowedByUser = followedBy(userId)
    return allUsers.map { user -> Friend(user, user.id in peopleFollowedByUser) }
  }

  private fun checkAccountExists(email: String) {
    if (allUsers.any { it.email == email }) {
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