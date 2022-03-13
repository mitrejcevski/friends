package nl.jovmit.friends.domain.friends

import nl.jovmit.friends.domain.exceptions.BackendException
import nl.jovmit.friends.domain.exceptions.ConnectionUnavailableException
import nl.jovmit.friends.domain.user.UserCatalog
import nl.jovmit.friends.friends.state.FollowState
import nl.jovmit.friends.friends.state.FriendsState

class FriendsRepository(
  private val userCatalog: UserCatalog
) {

  suspend fun loadFriendsFor(userId: String): FriendsState {
    return try {
      val friendsForUser = userCatalog.loadFriendsFor(userId)
      FriendsState.Loaded(friendsForUser)
    } catch (backendException: BackendException) {
      FriendsState.BackendError
    } catch (offlineException: ConnectionUnavailableException) {
      FriendsState.Offline
    }
  }

  suspend fun updateFollowing(userId: String, followeeId: String): FollowState {
    return try {
      toggleFollowing(userId, followeeId)
    } catch (backendException: BackendException) {
      FollowState.BackendError
    } catch (offlineException: ConnectionUnavailableException) {
      FollowState.Offline
    }
  }

  private suspend fun toggleFollowing(userId: String, followeeId: String): FollowState {
    val toggleResult = userCatalog.toggleFollowing(userId, followeeId)
    return if (toggleResult.isAdded) {
      FollowState.Followed(toggleResult.following)
    } else {
      FollowState.Unfollowed(toggleResult.following)
    }
  }
}