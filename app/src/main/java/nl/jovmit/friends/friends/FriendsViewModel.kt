package nl.jovmit.friends.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jovmit.friends.R
import nl.jovmit.friends.app.CoroutineDispatchers
import nl.jovmit.friends.domain.exceptions.BackendException
import nl.jovmit.friends.domain.friends.FriendsRepository
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
    viewModelScope.launch {
      updateListOfTogglingFriendships(followeeId)
      val updateFollowing = withContext(dispatchers.background) {
        try {
          friendsRepository.updateFollowing(userId, followeeId)
        } catch (e: BackendException) {
          errorUpdatingFollowing(followeeId)
        }
      }
      when (updateFollowing) {
        is FollowState.Followed -> updateFollowingState(updateFollowing.following.followedId, true)
        is FollowState.Unfollowed -> updateFollowingState(updateFollowing.following.followedId, false)
      }
    }
  }

  private fun errorUpdatingFollowing(followeeId: String) {
    val currentState = savedStateHandle[SCREEN_STATE_KEY] ?: FriendsScreenState()
    val newState = currentState.copy(
      error = R.string.errorFollowingFriend,
      currentlyUpdatingFriends = currentState.currentlyUpdatingFriends - listOf(followeeId)
    )
    savedStateHandle[SCREEN_STATE_KEY] = newState
  }

  private fun updateListOfTogglingFriendships(followeeId: String) {
    val currentState = savedStateHandle[SCREEN_STATE_KEY] ?: FriendsScreenState()
    val updatedList = currentState.currentlyUpdatingFriends + listOf(followeeId)
    savedStateHandle[SCREEN_STATE_KEY] = currentState.copy(currentlyUpdatingFriends = updatedList)
  }

  private fun updateFollowingState(followedId: String, isFollowee: Boolean) {
    val currentState = savedStateHandle[SCREEN_STATE_KEY] ?: FriendsScreenState()
    val index = currentState.friends.indexOfFirst { it.user.id == followedId }
    val matchingUser = currentState.friends[index]
    val updatedFriends = currentState.friends.toMutableList()
      .apply { set(index, matchingUser.copy(isFollowee = isFollowee)) }
    val updatedToggles = currentState.currentlyUpdatingFriends - listOf(followedId)
    val updatedState = currentState.copy(friends = updatedFriends, currentlyUpdatingFriends = updatedToggles)
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
