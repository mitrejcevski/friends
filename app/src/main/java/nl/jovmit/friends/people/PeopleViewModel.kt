package nl.jovmit.friends.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.jovmit.friends.domain.exceptions.BackendException
import nl.jovmit.friends.domain.exceptions.ConnectionUnavailableException
import nl.jovmit.friends.domain.user.Friend
import nl.jovmit.friends.domain.user.User
import nl.jovmit.friends.people.state.PeopleState

class PeopleViewModel {

  private val mutablePeopleState = MutableLiveData<PeopleState>()
  val peopleState: LiveData<PeopleState> = mutablePeopleState

  fun loadPeople(userId: String) {
    val result = if (userId.isBlank()) {
      PeopleState.Offline
    } else if (InMemoryPeopleCatalog().isKnownUser(userId)) {
      PeopleState.Loaded(InMemoryPeopleCatalog().loadPeopleFor(userId))
    } else if (!InMemoryPeopleCatalog().isKnownUser(userId)) {
      PeopleState.BackendError
    } else {
      TODO()
    }

    mutablePeopleState.value = result
  }

  class InMemoryPeopleCatalog {
    private val tom = Friend(User("tomId", "", ""), isFollowee = false)
    private val anna = Friend(User("annaId", "", ""), isFollowee = true)
    private val sara = Friend(User("saraId", "", ""), isFollowee = false)
    private val peopleForUserId = mapOf(
      "annaId" to listOf(tom),
      "lucyId" to listOf(anna, sara, tom),
      "saraId" to emptyList()
    )

    fun loadPeopleFor(userId: String): List<Friend> {
      if(userId.isBlank()) throw ConnectionUnavailableException()
      return peopleForUserId[userId] ?: throw BackendException()
    }

    fun isKnownUser(userId: String): Boolean {
      return peopleForUserId.containsKey(userId)
    }
  }
}