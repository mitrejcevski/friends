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
    if (userId == "annaId") {
      val tom = Friend(User("tomId", "", ""), isFollowee = false)
      mutablePeopleState.value = PeopleState.Loaded(listOf(tom))
    } else if (userId == "lucyId") {
      val anna = Friend(User("annaId", "", ""), isFollowee = true)
      val sara = Friend(User("saraId", "", ""), isFollowee = false)
      val tom = Friend(User("tomId", "", ""), isFollowee = false)
      mutablePeopleState.value = PeopleState.Loaded(listOf(anna, sara, tom))
    } else if (userId == "saraId") {
      mutablePeopleState.value = PeopleState.Loaded(emptyList())
    } else if (userId == "jovId") {
      mutablePeopleState.value = PeopleState.BackendError
    }
  }
}