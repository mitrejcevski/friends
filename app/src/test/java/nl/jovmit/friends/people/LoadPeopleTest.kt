package nl.jovmit.friends.people

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.app.TestDispatchers
import nl.jovmit.friends.domain.people.InMemoryPeopleCatalog
import nl.jovmit.friends.domain.people.PeopleRepository
import nl.jovmit.friends.domain.user.Friend
import nl.jovmit.friends.domain.user.User
import nl.jovmit.friends.people.state.PeopleState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class LoadPeopleTest {

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
  fun noPeopleExisting() {
    val viewModel = PeopleViewModel(PeopleRepository(peopleCatalog), TestDispatchers())

    viewModel.loadPeople("saraId")

    assertEquals(PeopleState.Loaded(emptyList()), viewModel.peopleState.value)
  }

  @Test
  fun loadedASinglePerson() {
    val viewModel = PeopleViewModel(PeopleRepository(peopleCatalog), TestDispatchers())

    viewModel.loadPeople("annaId")

    assertEquals(PeopleState.Loaded(listOf(tom)), viewModel.peopleState.value)
  }

  @Test
  fun loadedMultiplePeople() {
    val viewModel = PeopleViewModel(PeopleRepository(peopleCatalog), TestDispatchers())

    viewModel.loadPeople("lucyId")

    assertEquals(
      PeopleState.Loaded(listOf(anna, sara, tom)),
      viewModel.peopleState.value
    )
  }
}