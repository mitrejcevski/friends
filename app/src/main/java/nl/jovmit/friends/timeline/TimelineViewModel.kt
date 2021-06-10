package nl.jovmit.friends.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.jovmit.friends.domain.exceptions.BackendException
import nl.jovmit.friends.domain.exceptions.ConnectionUnavailableException
import nl.jovmit.friends.domain.post.PostCatalog
import nl.jovmit.friends.domain.user.UserCatalog
import nl.jovmit.friends.timeline.state.TimelineState

class TimelineViewModel(
  private val userCatalog: UserCatalog,
  private val postCatalog: PostCatalog
) {

  private val mutableTimelineState = MutableLiveData<TimelineState>()
  val timelineState: LiveData<TimelineState> = mutableTimelineState

  fun timelineFor(userId: String) {
    try {
      val userIds = listOf(userId) + userCatalog.followedBy(userId)
      val postsForUser = postCatalog.postsFor(userIds)
      mutableTimelineState.value = TimelineState.Posts(postsForUser)
    } catch (backendException: BackendException) {
      mutableTimelineState.value = TimelineState.BackendError
    } catch (offlineException: ConnectionUnavailableException) {
      mutableTimelineState.value = TimelineState.OfflineError
    }
  }
}
