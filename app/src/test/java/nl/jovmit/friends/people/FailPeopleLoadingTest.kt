package nl.jovmit.friends.people

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.people.state.PeopleState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class FailPeopleLoadingTest {

  @Test
  fun backendError() {
    val viewModel = PeopleViewModel()

    viewModel.loadPeople("jovId")

    assertEquals(PeopleState.BackendError, viewModel.peopleState.value)
  }

  @Test
  fun offlineError() {
    val viewModel = PeopleViewModel()

    viewModel.loadPeople("")

    assertEquals(PeopleState.Offline, viewModel.peopleState.value)
  }
}