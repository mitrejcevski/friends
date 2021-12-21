package nl.jovmit.friends.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import nl.jovmit.friends.domain.people.PeopleRepository
import nl.jovmit.friends.people.state.PeopleState

class PeopleViewModel(
  private val peopleRepository: PeopleRepository
): ViewModel() {

  private val mutablePeopleState = MutableLiveData<PeopleState>()
  val peopleState: LiveData<PeopleState> = mutablePeopleState

  fun loadPeople(userId: String) {
    viewModelScope.launch {
      val result = peopleRepository.loadPeopleFor(userId)
      mutablePeopleState.value = result
    }
  }
}