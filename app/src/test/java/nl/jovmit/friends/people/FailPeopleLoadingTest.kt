package nl.jovmit.friends.people

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.domain.exceptions.ConnectionUnavailableException
import nl.jovmit.friends.domain.people.InMemoryPeopleCatalog
import nl.jovmit.friends.domain.people.PeopleCatalog
import nl.jovmit.friends.domain.people.PeopleRepository
import nl.jovmit.friends.domain.user.Friend
import nl.jovmit.friends.domain.user.User
import nl.jovmit.friends.people.state.PeopleState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class FailPeopleLoadingTest {

  private val tom = Friend(User("tomId", "", ""), isFollowee = false)
  private val anna = Friend(User("annaId", "", ""), isFollowee = true)
  private val sara = Friend(User("saraId", "", ""), isFollowee = false)
  private val peopleCatalog = InMemoryPeopleCatalog(
    mapOf(
      "annaId" to listOf(tom),
      "lucyId" to listOf(anna, sara, tom),
      "saraId" to emptyList()
    )
  )

  @Test
  fun backendError() {
    val viewModel = PeopleViewModel(PeopleRepository(peopleCatalog))

    viewModel.loadPeople("jovId")

    assertEquals(PeopleState.BackendError, viewModel.peopleState.value)
  }

  @Test
  fun offlineError() {
    val viewModel = PeopleViewModel(PeopleRepository(OfflinePeopleCatalog()))

    viewModel.loadPeople(":irrelevant:")

    assertEquals(PeopleState.Offline, viewModel.peopleState.value)
  }

  private class OfflinePeopleCatalog : PeopleCatalog {

    override fun loadPeopleFor(userId: String): List<Friend> {
      throw ConnectionUnavailableException()
    }
  }
}