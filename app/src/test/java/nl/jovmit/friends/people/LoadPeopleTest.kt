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

  private val tom = User("tomId", "", "")
  private val anna = User("annaId", "", "")
  private val sara = User("saraId", "", "")
  private val friendTom = Friend(tom, isFollower = false)
  private val friendAnna = Friend(anna, isFollower = true)
  private val friendSara = Friend(sara, isFollower = false)

  private val peopleCatalog = InMemoryPeopleCatalog(
    mapOf(
      "annaId" to listOf(friendTom),
      "lucyId" to listOf(friendAnna, friendSara, friendTom),
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

    assertEquals(PeopleState.Loaded(listOf(friendTom)), viewModel.peopleState.value)
  }

  @Test
  fun loadedMultiplePeople() {
    val viewModel = PeopleViewModel(PeopleRepository(peopleCatalog), TestDispatchers())

    viewModel.loadPeople("lucyId")

    assertEquals(
      PeopleState.Loaded(listOf(friendAnna, friendSara, friendTom)),
      viewModel.peopleState.value
    )
  }
}