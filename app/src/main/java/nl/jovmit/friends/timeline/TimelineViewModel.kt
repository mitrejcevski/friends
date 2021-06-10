package nl.jovmit.friends.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.jovmit.friends.domain.exceptions.BackendException
import nl.jovmit.friends.domain.exceptions.ConnectionUnavailableException
import nl.jovmit.friends.domain.post.PostCatalog
import nl.jovmit.friends.domain.timeline.TimelineRepository
import nl.jovmit.friends.domain.user.UserCatalog
import nl.jovmit.friends.timeline.state.TimelineState

class TimelineViewModel(
  private val userCatalog: UserCatalog,
  private val postCatalog: PostCatalog
) {

  private val mutableTimelineState = MutableLiveData<TimelineState>()
  val timelineState: LiveData<TimelineState> = mutableTimelineState

  fun timelineFor(userId: String) {
    val result = TimelineRepository(userCatalog, postCatalog)
      .getTimelineFor(userId)
    mutableTimelineState.value = result
  }
}