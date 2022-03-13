package nl.jovmit.friends.friends

import androidx.lifecycle.SavedStateHandle
import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.R
import nl.jovmit.friends.app.TestDispatchers
import nl.jovmit.friends.domain.friends.FriendsRepository
import nl.jovmit.friends.domain.user.OfflineUserCatalog
import nl.jovmit.friends.domain.user.UnavailableUserCatalog
import nl.jovmit.friends.friends.state.FriendsScreenState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class FailUpdatingFollowingTest {

  private val dispatchers = TestDispatchers()
  private val savedStateHandle = SavedStateHandle()

  @Test
  fun backendError() {
    val friendsRepository = FriendsRepository(UnavailableUserCatalog())
    val viewModel = FriendsViewModel(friendsRepository, dispatchers, savedStateHandle)

    viewModel.toggleFollowing(":userId:", ":followeeId:")

    assertEquals(
      FriendsScreenState(error = R.string.errorFollowingFriend),
      viewModel.screenState.value
    )
  }

  @Test
  fun offlineError() {
    val friendsRepository = FriendsRepository(OfflineUserCatalog())
    val viewModel = FriendsViewModel(friendsRepository, dispatchers, savedStateHandle)

    viewModel.toggleFollowing(":userId:", ":followeeId:")

    assertEquals(
      FriendsScreenState(error = R.string.offlineError),
      viewModel.screenState.value
    )
  }
}