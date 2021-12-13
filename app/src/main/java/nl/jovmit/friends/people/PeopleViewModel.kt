package nl.jovmit.friends.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.jovmit.friends.people.state.PeopleState

class PeopleViewModel {

  private val mutablePeopleState = MutableLiveData<PeopleState>()
  val peopleState: LiveData<PeopleState> = mutablePeopleState

  fun loadPeople(userId: String) {
    mutablePeopleState.value = PeopleState.Loaded(emptyList())
  }
}