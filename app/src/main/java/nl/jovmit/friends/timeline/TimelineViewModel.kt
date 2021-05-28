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
      Post("post1", "lucyId", "post 1", 1L),
      Post("post4", "saraId", "post 4", 4L),
      Post("post3", "saraId", "post 3", 3L)
    )
    if (userId == "saraId") {
      val lucyPosts = availablePosts.filter { it.userId == "lucyId" }
      val saraPosts = availablePosts.filter { it.userId == "saraId" }
      mutableTimelineState.value = TimelineState.Posts(lucyPosts + saraPosts)
    } else if (userId == "annaId") {
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
