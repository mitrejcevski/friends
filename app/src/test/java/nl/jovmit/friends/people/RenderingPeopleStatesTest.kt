package nl.jovmit.friends.people

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.app.TestDispatchers
import nl.jovmit.friends.domain.people.InMemoryPeopleCatalog
import nl.jovmit.friends.domain.people.PeopleRepository
import nl.jovmit.friends.domain.user.Friend
import nl.jovmit.friends.infrastructure.builder.UserBuilder.Companion.aUser
import nl.jovmit.friends.people.state.PeopleState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class RenderingPeopleStatesTest {

  private val anna = Friend(aUser().withId("annaId").build(), isFollowee = true)
  private val tom = Friend(aUser().withId("tomId").build(), isFollowee = true)
  private val peopleCatalog = InMemoryPeopleCatalog(
    mapOf(
      "jovId" to listOf(tom, anna)
    )
  )

  @Test
  fun peopleStatesDeliveredToAnObserverInParticularOrder() {
    val viewModel = PeopleViewModel(PeopleRepository(peopleCatalog), TestDispatchers())
    val deliveredStates = mutableListOf<PeopleState>()
    viewModel.peopleState.observeForever { deliveredStates.add(it) }

    viewModel.loadPeople("jovId")

    assertEquals(
      listOf(PeopleState.Loading, PeopleState.Loaded(listOf(tom, anna))),
      deliveredStates
    )
  }
}