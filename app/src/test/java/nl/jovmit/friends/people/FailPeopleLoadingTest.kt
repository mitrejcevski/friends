package nl.jovmit.friends.people

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.app.TestDispatchers
import nl.jovmit.friends.domain.people.PeopleRepository
import nl.jovmit.friends.domain.user.OfflineUserCatalog
import nl.jovmit.friends.domain.user.UnavailableUserCatalog
import nl.jovmit.friends.people.state.PeopleState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class FailPeopleLoadingTest {

  @Test
  fun backendError() {
    val viewModel = PeopleViewModel(
      PeopleRepository(
        UnavailableUserCatalog()
      ), TestDispatchers()
    )

    viewModel.loadPeople(":irrelevant:")

    assertEquals(PeopleState.BackendError, viewModel.peopleState.value)
  }

  @Test
  fun offlineError() {
    val viewModel = PeopleViewModel(
      PeopleRepository(
        OfflineUserCatalog()
      ),
      TestDispatchers()
    )

    viewModel.loadPeople(":irrelevant:")

    assertEquals(PeopleState.Offline, viewModel.peopleState.value)
  }
}