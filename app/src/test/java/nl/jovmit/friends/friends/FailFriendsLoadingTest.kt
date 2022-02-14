package nl.jovmit.friends.friends

import androidx.lifecycle.SavedStateHandle
import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.app.TestDispatchers
import nl.jovmit.friends.domain.friends.FriendsRepository
import nl.jovmit.friends.domain.user.OfflineUserCatalog
import nl.jovmit.friends.domain.user.UnavailableUserCatalog
import nl.jovmit.friends.friends.state.FriendsState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class FailFriendsLoadingTest {

  @Test
  fun backendError() {
    val viewModel = FriendsViewModel(
      FriendsRepository(
        UnavailableUserCatalog()
      ), TestDispatchers(), SavedStateHandle()
    )

    viewModel.loadFriends(":irrelevant:")

    assertEquals(FriendsState.BackendError, viewModel.friendsState.value)
  }

  @Test
  fun offlineError() {
    val viewModel = FriendsViewModel(
      FriendsRepository(
        OfflineUserCatalog()
      ),
      TestDispatchers(),
      SavedStateHandle()
    )

    viewModel.loadFriends(":irrelevant:")

    assertEquals(FriendsState.Offline, viewModel.friendsState.value)
  }
}