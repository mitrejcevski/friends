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
import nl.jovmit.friends.domain.user.Following
import nl.jovmit.friends.friends.state.FollowState
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
    when (val result = updateFollowing(userId, followeeId)) {
      is FollowState.Followed -> updateFollowingState(result.following.followedId, true)
      is FollowState.Unfollowed -> updateFollowingState(result.following.followedId, false)
    }
  }

  private fun updateFollowing(userId: String, followeeId: String): FollowState {
    return if (userId == "annaId") {
      FollowState.Followed(Following(userId, followeeId))
    } else {
      FollowState.Unfollowed(Following(userId, followeeId))
    }
  }

  private fun updateFollowingState(followedId: String, isFollowee: Boolean) {
    val currentState = savedStateHandle[SCREEN_STATE_KEY] ?: FriendsScreenState()
    val index = currentState.friends.indexOfFirst { it.user.id == followedId }
    val matchingUser = currentState.friends[index]
    val updatedFriends = currentState.friends.toMutableList()
      .apply { set(index, matchingUser.copy(isFollowee = isFollowee)) }
    val updatedState = currentState.copy(friends = updatedFriends)
    savedStateHandle[SCREEN_STATE_KEY] = updatedState
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
