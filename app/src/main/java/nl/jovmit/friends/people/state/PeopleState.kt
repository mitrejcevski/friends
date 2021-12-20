package nl.jovmit.friends.people.state

import nl.jovmit.friends.domain.user.Friend

sealed class PeopleState {

  data class Loaded(val friends: List<Friend>) : PeopleState()

  object BackendError : PeopleState()

  object Offline : PeopleState()
}