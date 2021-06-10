package nl.jovmit.friends.timeline.state

import nl.jovmit.friends.domain.post.Post

sealed class TimelineState {

  data class Posts(val posts: List<Post>) : TimelineState()

  object BackendError : TimelineState()

  object OfflineError : TimelineState()
}
