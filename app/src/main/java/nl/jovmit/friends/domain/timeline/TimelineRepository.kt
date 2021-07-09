package nl.jovmit.friends.domain.timeline

import nl.jovmit.friends.domain.exceptions.BackendException
import nl.jovmit.friends.domain.exceptions.ConnectionUnavailableException
import nl.jovmit.friends.domain.post.PostCatalog
import nl.jovmit.friends.domain.user.UserCatalog
import nl.jovmit.friends.timeline.state.TimelineState

class TimelineRepository(
  private val userCatalog: UserCatalog,
  private val postCatalog: PostCatalog
) {

  suspend fun getTimelineFor(userId: String): TimelineState {
    return try {
      val userIds = listOf(userId) + userCatalog.followedBy(userId)
      val postsForUser = postCatalog.postsFor(userIds)
      TimelineState.Posts(postsForUser)
    } catch (backendException: BackendException) {
      TimelineState.BackendError
    } catch (offlineException: ConnectionUnavailableException) {
      TimelineState.OfflineError
    }
  }
}