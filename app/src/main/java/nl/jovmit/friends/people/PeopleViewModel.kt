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
    val result = if (userId == "annaId") {
      val tom = Friend(User("tomId", "", ""), isFollowee = false)
      PeopleState.Loaded(listOf(tom))
    } else if (userId == "lucyId") {
      val anna = Friend(User("annaId", "", ""), isFollowee = true)
      val sara = Friend(User("saraId", "", ""), isFollowee = false)
      val tom = Friend(User("tomId", "", ""), isFollowee = false)
      PeopleState.Loaded(listOf(anna, sara, tom))
    } else if (userId == "saraId") {
      PeopleState.Loaded(emptyList())
    } else if (userId == "jovId") {
      PeopleState.BackendError
    } else if (userId.isBlank()) {
      PeopleState.Offline
    } else {
      TODO()
    }
    mutablePeopleState.value = result
  }
}