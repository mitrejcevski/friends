package nl.jovmit.friends.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.jovmit.friends.domain.exceptions.BackendException
import nl.jovmit.friends.domain.exceptions.ConnectionUnavailableException
import nl.jovmit.friends.domain.people.InMemoryPeopleCatalog
import nl.jovmit.friends.people.state.PeopleState

class PeopleViewModel {

  private val mutablePeopleState = MutableLiveData<PeopleState>()
  val peopleState: LiveData<PeopleState> = mutablePeopleState

  fun loadPeople(userId: String) {
    val result = PeopleRepository().loadPeopleFor(userId)
    mutablePeopleState.value = result
  }

  class PeopleRepository {
    fun loadPeopleFor(userId: String): PeopleState {
      return try {
        val peopleForUserId = InMemoryPeopleCatalog().loadPeopleFor(userId)
        PeopleState.Loaded(peopleForUserId)
      } catch (backendException: BackendException) {
        PeopleState.BackendError
      } catch (offlineException: ConnectionUnavailableException) {
        PeopleState.Offline
      }
    }
  }
}