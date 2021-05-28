package nl.jovmit.friends.domain.post

data class Post(
  val id: String,
  val userId: String,
  val text: String,
  val timestamp: Long
)
