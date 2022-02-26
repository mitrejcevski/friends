package nl.jovmit.friends.domain.friends

import nl.jovmit.friends.domain.user.Following

data class ToggleFollowing(
  val following: Following,
  val isAdded: Boolean
)
