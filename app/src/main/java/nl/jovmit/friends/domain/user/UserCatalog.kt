package nl.jovmit.friends.domain.user

import nl.jovmit.friends.domain.friends.ToggleFollowing

interface UserCatalog {

  suspend fun createUser(
    email: String,
    password: String,
    about: String
  ): User

  fun toggleFollowing(userId: String, followeeId: String): ToggleFollowing

  suspend fun followedBy(userId: String): List<String>

  suspend fun loadFriendsFor(userId: String): List<Friend>
}