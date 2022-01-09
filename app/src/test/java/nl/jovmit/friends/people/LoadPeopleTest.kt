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
class LoadPeopleTest {

  private val tom = aUser().withId("tomId").build()
  private val anna = aUser().withId("annaId").build()
  private val sara = aUser().withId("saraId").build()
  private val lucy = aUser().withId("lucyId").build()
  private val friendTom = Friend(tom, isFollower = false)
  private val friendAnna = Friend(anna, isFollower = true)
  private val friendSara = Friend(sara, isFollower = false)

  private val peopleCatalog = InMemoryPeopleCatalog(
    mapOf(
      anna.id to listOf(friendTom),
      lucy.id to listOf(friendAnna, friendSara, friendTom),
      sara.id to emptyList()
    )
  )

  @Test
  fun noPeopleExisting() {
    val userCatalog = InMemoryUserCatalog()
    val viewModel = PeopleViewModel(PeopleRepository(peopleCatalog, userCatalog), TestDispatchers())

    viewModel.loadPeople(sara.id)

    assertEquals(PeopleState.Loaded(emptyList()), viewModel.peopleState.value)
  }

  @Test
  fun loadedASinglePerson() {
    val userCatalog = InMemoryUserCatalog(
      usersForPassword = mutableMapOf(":irrelevant" to mutableListOf(tom))
    )
    val viewModel = PeopleViewModel(PeopleRepository(peopleCatalog, userCatalog), TestDispatchers())

    viewModel.loadPeople(anna.id)

    assertEquals(PeopleState.Loaded(listOf(friendTom)), viewModel.peopleState.value)
  }

  @Test
  fun loadedMultiplePeople() {
    val userCatalog = InMemoryUserCatalog(
      usersForPassword = mutableMapOf(":irrelevant:" to mutableListOf(anna, sara, tom)),
      followings = mutableListOf(Following(lucy.id, anna.id))
    )
    val viewModel = PeopleViewModel(PeopleRepository(peopleCatalog, userCatalog), TestDispatchers())

    viewModel.loadPeople(lucy.id)

    assertEquals(
      PeopleState.Loaded(listOf(friendAnna, friendSara, friendTom)),
      viewModel.peopleState.value
    )
  }
}