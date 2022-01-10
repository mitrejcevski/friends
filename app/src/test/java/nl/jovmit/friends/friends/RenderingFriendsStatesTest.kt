package nl.jovmit.friends.friends

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.app.TestDispatchers
import nl.jovmit.friends.domain.friends.FriendsRepository
import nl.jovmit.friends.domain.user.Following
import nl.jovmit.friends.domain.user.Friend
import nl.jovmit.friends.domain.user.InMemoryUserCatalog
import nl.jovmit.friends.infrastructure.builder.UserBuilder.Companion.aUser
import nl.jovmit.friends.friends.state.FriendsState
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
    TestDispatchers()
  )

  @Test
  fun friendsStatesDeliveredToAnObserverInParticularOrder() {
    val deliveredStates = mutableListOf<FriendsState>()
    viewModel.friendsState.observeForever { deliveredStates.add(it) }

    viewModel.loadFriends(jov.id)

    assertEquals(
      listOf(FriendsState.Loading, FriendsState.Loaded(listOf(friendTom, friendAnna))),
      deliveredStates
    )
  }
}