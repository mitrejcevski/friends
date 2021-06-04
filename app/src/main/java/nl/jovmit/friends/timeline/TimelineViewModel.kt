package nl.jovmit.friends.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.jovmit.friends.domain.post.InMemoryPostCatalog
import nl.jovmit.friends.domain.user.InMemoryUserCatalog
import nl.jovmit.friends.timeline.state.TimelineState

class TimelineViewModel(
  private val userCatalog: InMemoryUserCatalog
  ) {

  private val mutableTimelineState = MutableLiveData<TimelineState>()
  val timelineState: LiveData<TimelineState> = mutableTimelineState

  fun timelineFor(userId: String) {
    val userIds = listOf(userId) + userCatalog.followedBy(userId)
    val postsForUser = InMemoryPostCatalog().postsFor(userIds)
    mutableTimelineState.value = TimelineState.Posts(postsForUser)
  }
}
