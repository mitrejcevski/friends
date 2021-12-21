package nl.jovmit.friends.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jovmit.friends.app.CoroutineDispatchers
import nl.jovmit.friends.domain.people.PeopleRepository
import nl.jovmit.friends.people.state.PeopleState

class PeopleViewModel(
  private val peopleRepository: PeopleRepository,
  private val dispatchers: CoroutineDispatchers
) : ViewModel() {

  private val mutablePeopleState = MutableLiveData<PeopleState>()
  val peopleState: LiveData<PeopleState> = mutablePeopleState

  fun loadPeople(userId: String) {
    viewModelScope.launch {
      val result = withContext(dispatchers.background) {
        peopleRepository.loadPeopleFor(userId)
      }
      mutablePeopleState.value = result
    }
  }
}