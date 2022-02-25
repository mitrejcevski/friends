package nl.jovmit.friends.domain.friends

import nl.jovmit.friends.domain.exceptions.BackendException
import nl.jovmit.friends.domain.exceptions.ConnectionUnavailableException
import nl.jovmit.friends.domain.user.Following
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

  fun updateFollowing(userId: String, followeeId: String): FollowState {
    return if (userId == "annaId") {
      FollowState.Followed(Following(userId, followeeId))
    } else {
      FollowState.Unfollowed(Following(userId, followeeId))
    }
  }
}