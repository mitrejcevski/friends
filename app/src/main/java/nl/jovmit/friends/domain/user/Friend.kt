package nl.jovmit.friends.domain.user

data class Friend(
  val user: User,
  val isFollower: Boolean
)