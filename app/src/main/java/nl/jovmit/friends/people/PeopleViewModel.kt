package nl.jovmit.friends.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.jovmit.friends.domain.user.Friend
import nl.jovmit.friends.domain.user.User
import nl.jovmit.friends.people.state.PeopleState

class PeopleViewModel {

  private val mutablePeopleState = MutableLiveData<PeopleState>()
  val peopleState: LiveData<PeopleState> = mutablePeopleState

  fun loadPeople(userId: String) {
    val result = if (userId.isBlank()) {
      PeopleState.Offline
    } else if (isKnownUser(userId)) {
      PeopleState.Loaded(loadPeopleFor(userId))
    } else if (!isKnownUser(userId)) {
      PeopleState.BackendError
    } else {
      TODO()
    }
    mutablePeopleState.value = result
  }

  val tom = Friend(User("tomId", "", ""), isFollowee = false)
  val anna = Friend(User("annaId", "", ""), isFollowee = true)
  val sara = Friend(User("saraId", "", ""), isFollowee = false)
  val peopleForUserId = mapOf(
    "annaId" to listOf(tom),
    "lucyId" to listOf(anna, sara, tom),
    "saraId" to emptyList()
  )

  private fun loadPeopleFor(userId: String): List<Friend> {
    return peopleForUserId.getValue(userId)
  }

  private fun isKnownUser(userId: String): Boolean {
    return peopleForUserId.containsKey(userId)
  }
}