package nl.jovmit.friends.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.jovmit.friends.domain.post.Post
import nl.jovmit.friends.timeline.state.TimelineState

class TimelineViewModel {

  private val mutableTimelineState = MutableLiveData<TimelineState>()
  val timelineState: LiveData<TimelineState> = mutableTimelineState

  fun timelineFor(userId: String) {
    val availablePosts = listOf(
      Post("postId", "timId", "post text", 1L),
      Post("post2", "lucyId", "post 2", 2L),
      Post("post1", "lucyId", "post 1", 1L)
    )
    if (userId == "annaId") {
      val annaPosts = availablePosts.filter { it.userId == "lucyId" }
      mutableTimelineState.value = TimelineState.Posts(annaPosts)
    } else if (userId == "timId") {
      val timPosts = availablePosts.filter { it.userId == "timId" }
      mutableTimelineState.value = TimelineState.Posts(timPosts)
    } else {
      mutableTimelineState.value = TimelineState.Posts(emptyList())
    }
  }
}
