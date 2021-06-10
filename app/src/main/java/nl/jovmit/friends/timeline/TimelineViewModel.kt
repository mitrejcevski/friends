package nl.jovmit.friends.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    val userIds = listOf(userId) + userCatalog.followedBy(userId)
    val postsForUser = postCatalog.postsFor(userIds)
    mutableTimelineState.value = TimelineState.Posts(postsForUser)
  }
}
