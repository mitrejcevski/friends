package nl.jovmit.friends.friends.state

import nl.jovmit.friends.domain.user.Friend

sealed class FriendsState {

  object Loading : FriendsState()

  data class Loaded(val friends: List<Friend>) : FriendsState()

  object BackendError : FriendsState()

  object Offline : FriendsState()
}