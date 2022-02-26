package nl.jovmit.friends.domain.user

import nl.jovmit.friends.domain.exceptions.DuplicateAccountException
import nl.jovmit.friends.domain.friends.ToggleFollowing

class InMemoryUserCatalog(
  private val usersForPassword: MutableMap<String, MutableList<User>> = mutableMapOf(),
  private val followings: MutableList<Following> = mutableListOf()
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

  override fun toggleFollowing(userId: String, followeeId: String): ToggleFollowing {
    val following = Following(userId, followeeId)
    return if (followings.contains(following)) {
      followings.remove(following)
      ToggleFollowing(following, false)
    } else {
      followings.add(following)
      ToggleFollowing(following, true)
    }
  }

  override suspend fun followedBy(userId: String): List<String> {
    return followings
      .filter { it.userId == userId }
      .map { it.followedId }
  }

  override suspend fun loadFriendsFor(userId: String): List<Friend> {
    val friendsFollowedByUser = followedBy(userId)
    return allUsers
      .filter { it.id != userId }
      .map { user -> Friend(user, user.id in friendsFollowedByUser) }
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