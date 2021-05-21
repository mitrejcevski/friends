package nl.jovmit.friends.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.jovmit.friends.domain.post.Post
import nl.jovmit.friends.timeline.state.TimelineState

class TimelineViewModel {

  private val mutableTimelineState = MutableLiveData<TimelineState>()
  val timelineState: LiveData<TimelineState> = mutableTimelineState

  fun timelineFor(userId: String) {
    if(userId == "timId") {
      val posts = listOf(Post("postId", "timId", "post text", 1L))
      mutableTimelineState.value = TimelineState.Posts(posts)
    } else {
      mutableTimelineState.value = TimelineState.Posts(emptyList())
    }
  }
}
