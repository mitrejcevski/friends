package nl.jovmit.friends.friends.state

import nl.jovmit.friends.domain.user.Following

sealed class FollowState {

  data class Followed(val following: Following) : FollowState()

  data class Unfollowed(val following: Following) : FollowState()

  object BackendError : FollowState()

  object Offline : FollowState()
}