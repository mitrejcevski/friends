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
class ToggleFollowingTest {

  private val dispatchers = TestDispatchers()
  private val savedStateHandle = SavedStateHandle()

  private val anna = aUser().withId("annaId").build()
  private val tom = aUser().withId("tomId").build()
  private val users = mutableMapOf(":irrelevant:" to mutableListOf(anna, tom))

  @Test
  fun follow() {
    val repository = FriendsRepository(InMemoryUserCatalog(users))
    val viewModel = FriendsViewModel(repository, dispatchers, savedStateHandle).apply {
      loadFriends(anna.id)
    }

    viewModel.toggleFollowing(anna.id, tom.id)

    assertEquals(
      FriendsScreenState(friends = listOf(Friend(tom, isFollowee = true))),
      viewModel.screenState.value
    )
  }

  @Test
  fun unfollow() {
    val followings = listOf(Following(tom.id, anna.id))
    val repository = FriendsRepository(InMemoryUserCatalog(users, followings))
    val viewModel = FriendsViewModel(repository, dispatchers, savedStateHandle).apply {
      loadFriends(tom.id)
    }

    viewModel.toggleFollowing(tom.id, anna.id)

    assertEquals(
      FriendsScreenState(friends = listOf(Friend(anna, isFollowee = false))),
      viewModel.screenState.value
    )
  }
}