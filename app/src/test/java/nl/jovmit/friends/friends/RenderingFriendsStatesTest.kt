package nl.jovmit.friends.friends

import androidx.lifecycle.SavedStateHandle
import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.app.TestDispatchers
import nl.jovmit.friends.domain.friends.FriendsRepository
import nl.jovmit.friends.domain.user.Following
import nl.jovmit.friends.domain.user.Friend
import nl.jovmit.friends.domain.user.InMemoryUserCatalog
import nl.jovmit.friends.friends.state.FriendsScreenState
import nl.jovmit.friends.infrastructure.builder.UserBuilder.Companion.aUser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class RenderingFriendsStatesTest {

  private val anna = aUser().withId("annaId").build()
  private val tom = aUser().withId("tomId").build()
  private val jov = aUser().withId("jovId").build()
  private val friendAnna = Friend(anna, isFollower = true)
  private val friendTom = Friend(tom, isFollower = true)

  private val userCatalog = InMemoryUserCatalog(
    usersForPassword = mutableMapOf(":irrelevant:" to mutableListOf(tom, anna)),
    followings = mutableListOf(Following(jov.id, anna.id), Following(jov.id, tom.id))
  )
  private val viewModel = FriendsViewModel(
    FriendsRepository(userCatalog),
    TestDispatchers(),
    SavedStateHandle()
  )

  @Test
  fun friendsStatesDeliveredToAnObserverInParticularOrder() {
    val loading = FriendsScreenState(isLoading = true)
    val loaded = FriendsScreenState(friends = listOf(friendTom, friendAnna))
    val deliveredStates = mutableListOf<FriendsScreenState>()
    viewModel.screenState.observeForever { deliveredStates.add(it) }

    viewModel.loadFriends(jov.id)

    assertEquals(listOf(loading, loaded), deliveredStates)
  }
}