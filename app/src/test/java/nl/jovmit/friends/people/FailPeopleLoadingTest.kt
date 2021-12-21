package nl.jovmit.friends.people

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.domain.exceptions.BackendException
import nl.jovmit.friends.domain.exceptions.ConnectionUnavailableException
import nl.jovmit.friends.domain.people.PeopleCatalog
import nl.jovmit.friends.domain.people.PeopleRepository
import nl.jovmit.friends.domain.user.Friend
import nl.jovmit.friends.people.state.PeopleState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class FailPeopleLoadingTest {

  @Test
  fun backendError() {
    val viewModel = PeopleViewModel(PeopleRepository(UnavailablePeopleCatalog()))

    viewModel.loadPeople(":irrelevant:")

    assertEquals(PeopleState.BackendError, viewModel.peopleState.value)
  }

  @Test
  fun offlineError() {
    val viewModel = PeopleViewModel(PeopleRepository(OfflinePeopleCatalog()))

    viewModel.loadPeople(":irrelevant:")

    assertEquals(PeopleState.Offline, viewModel.peopleState.value)
  }

  private class UnavailablePeopleCatalog : PeopleCatalog {

    override suspend fun loadPeopleFor(userId: String): List<Friend> {
      throw BackendException()
    }
  }

  private class OfflinePeopleCatalog : PeopleCatalog {

    override suspend fun loadPeopleFor(userId: String): List<Friend> {
      throw ConnectionUnavailableException()
    }
  }
}