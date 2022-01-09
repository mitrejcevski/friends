package nl.jovmit.friends.people

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.app.TestDispatchers
import nl.jovmit.friends.domain.people.InMemoryPeopleCatalog
import nl.jovmit.friends.domain.people.PeopleRepository
import nl.jovmit.friends.domain.user.Following
import nl.jovmit.friends.domain.user.Friend
import nl.jovmit.friends.domain.user.InMemoryUserCatalog
import nl.jovmit.friends.infrastructure.builder.UserBuilder.Companion.aUser
import nl.jovmit.friends.people.state.PeopleState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class RenderingPeopleStatesTest {

  private val anna = aUser().withId("annaId").build()
  private val tom = aUser().withId("tomId").build()
  private val jov = aUser().withId("jovId").build()
  private val friendAnna = Friend(anna, isFollower = true)
  private val friendTom = Friend(tom, isFollower = true)

  private val peopleCatalog = InMemoryPeopleCatalog(
    mapOf(
      jov.id to listOf(friendTom, friendAnna)
    )
  )
  private val userCatalog = InMemoryUserCatalog(
    usersForPassword = mutableMapOf(":irrelevant:" to mutableListOf(tom, anna)),
    followings = mutableListOf(Following(jov.id, anna.id), Following(jov.id, tom.id))
  )
  private val viewModel = PeopleViewModel(
    PeopleRepository(peopleCatalog, userCatalog),
    TestDispatchers()
  )

  @Test
  fun peopleStatesDeliveredToAnObserverInParticularOrder() {
    val deliveredStates = mutableListOf<PeopleState>()
    viewModel.peopleState.observeForever { deliveredStates.add(it) }

    viewModel.loadPeople(jov.id)

    assertEquals(
      listOf(PeopleState.Loading, PeopleState.Loaded(listOf(friendTom, friendAnna))),
      deliveredStates
    )
  }
}