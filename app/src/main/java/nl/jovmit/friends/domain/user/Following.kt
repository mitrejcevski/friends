package nl.jovmit.friends.domain.user

data class Following(
  val userId: String,
  val followedId: String
)