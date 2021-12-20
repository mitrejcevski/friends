package nl.jovmit.friends.people

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.domain.people.InMemoryPeopleCatalog
import nl.jovmit.friends.domain.people.PeopleRepository
import nl.jovmit.friends.domain.user.Friend
import nl.jovmit.friends.domain.user.User
import nl.jovmit.friends.people.state.PeopleState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class FailPeopleLoadingTest {

  @Test
  fun backendError() {
    val viewModel = PeopleViewModel(PeopleRepository(InMemoryPeopleCatalog(
      mapOf(
        "annaId" to listOf(Friend(User("tomId", "", ""), isFollowee = false)),
        "lucyId" to listOf(
          Friend(User("annaId", "", ""), isFollowee = true),
          Friend(User("saraId", "", ""), isFollowee = false),
          Friend(User("tomId", "", ""), isFollowee = false)
        ),
        "saraId" to emptyList()
      )
    )
    ))

    viewModel.loadPeople("jovId")

    assertEquals(PeopleState.BackendError, viewModel.peopleState.value)
  }

  @Test
  fun offlineError() {
    val viewModel = PeopleViewModel(PeopleRepository(InMemoryPeopleCatalog(
      mapOf(
        "annaId" to listOf(Friend(User("tomId", "", ""), isFollowee = false)),
        "lucyId" to listOf(
          Friend(User("annaId", "", ""), isFollowee = true),
          Friend(User("saraId", "", ""), isFollowee = false),
          Friend(User("tomId", "", ""), isFollowee = false)
        ),
        "saraId" to emptyList()
      )
    )
    ))

    viewModel.loadPeople("")

    assertEquals(PeopleState.Offline, viewModel.peopleState.value)
  }
}