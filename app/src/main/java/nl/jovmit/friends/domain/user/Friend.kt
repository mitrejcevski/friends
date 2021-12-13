package nl.jovmit.friends.domain.user

data class Friend(
  val user: User,
  val isFollowee: Boolean
)