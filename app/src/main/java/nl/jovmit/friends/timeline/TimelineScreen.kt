package nl.jovmit.friends.timeline

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import nl.jovmit.friends.R
import nl.jovmit.friends.domain.post.Post
import nl.jovmit.friends.timeline.state.TimelineState
import nl.jovmit.friends.ui.composables.ScreenTitle

class TimelineScreenState {
  var posts by mutableStateOf(emptyList<Post>())

  fun updatePosts(newPosts: List<Post>) {
    this.posts = newPosts
  }
}

@Composable
fun TimelineScreen(
  userId: String,
  timelineViewModel: TimelineViewModel
) {
  val screenState by remember { mutableStateOf(TimelineScreenState()) }
  val timelineState by timelineViewModel.timelineState.observeAsState()
  timelineViewModel.timelineFor(userId)

  if (timelineState is TimelineState.Posts) {
    val posts = (timelineState as TimelineState.Posts).posts
    screenState.updatePosts(posts)
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    ScreenTitle(resource = R.string.timeline)
    PostsList(screenState.posts)
  }
}

@Composable
private fun PostsList(posts: List<Post>) {
  Text(text = stringResource(id = R.string.emptyTimelineMessage))
}
