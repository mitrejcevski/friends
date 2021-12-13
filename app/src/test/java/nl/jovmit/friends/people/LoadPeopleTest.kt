package nl.jovmit.friends.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.domain.user.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class LoadPeopleTest {

  @Test
  fun noPeopleExisting() {
    val viewModel = PeopleViewModel()

    viewModel.loadPeople("saraId")

    assertEquals(PeopleState.Loaded(emptyList()), viewModel.peopleState.value)
  }

  class PeopleViewModel {

    private val mutablePeopleState = MutableLiveData<PeopleState>()
    val peopleState: LiveData<PeopleState> = mutablePeopleState

    fun loadPeople(userId: String) {
      mutablePeopleState.value = PeopleState.Loaded(emptyList())
    }
  }

  sealed class PeopleState {

    data class Loaded(val friends: List<Friend>): PeopleState()
  }

  data class Friend(
    val user: User,
    val isFollowee: Boolean
  )
}