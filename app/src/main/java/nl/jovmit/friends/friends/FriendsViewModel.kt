package nl.jovmit.friends.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jovmit.friends.R
import nl.jovmit.friends.app.CoroutineDispatchers
import nl.jovmit.friends.domain.friends.FriendsRepository
import nl.jovmit.friends.domain.user.Friend
import nl.jovmit.friends.domain.user.User
import nl.jovmit.friends.friends.state.FriendsScreenState
import nl.jovmit.friends.friends.state.FriendsState

class FriendsViewModel(
  private val friendsRepository: FriendsRepository,
  private val dispatchers: CoroutineDispatchers,
  private val savedStateHandle: SavedStateHandle
) : ViewModel() {

  val screenState: LiveData<FriendsScreenState> =
    savedStateHandle.getLiveData(SCREEN_STATE_KEY)

  fun loadFriends(userId: String) {
    viewModelScope.launch {
      updateScreenState(FriendsState.Loading)
      val result = withContext(dispatchers.background) {
        friendsRepository.loadFriendsFor(userId)
      }
      updateScreenState(result)
    }
  }

  fun toggleFollowing(userId: String, followeeId: String) {
    val user = User(followeeId, "user@friends.com", "About User")
    val newState = FriendsScreenState(friends = listOf(Friend(user, true)))
    savedStateHandle[SCREEN_STATE_KEY] = newState
  }

  private fun updateScreenState(friendsState: FriendsState) {
    val currentState = savedStateHandle[SCREEN_STATE_KEY] ?: FriendsScreenState()
    val newState = when (friendsState) {
      is FriendsState.Loading -> currentState.copy(isLoading = true)
      is FriendsState.Loaded -> currentState.copy(isLoading = false, friends = friendsState.friends)
      is FriendsState.BackendError ->
        currentState.copy(isLoading = false, error = R.string.fetchingFriendsError)
      is FriendsState.Offline -> currentState.copy(isLoading = false, error = R.string.offlineError)
    }
    savedStateHandle[SCREEN_STATE_KEY] = newState
  }

  private companion object {
    private const val SCREEN_STATE_KEY = "friendsScreenState"
  }
}
