package nl.jovmit.friends.timeline

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
  if (posts.isEmpty()) {
    Text(text = stringResource(id = R.string.emptyTimelineMessage))
  } else {
    LazyColumn {
      items(posts) { post ->
        PostItem(post = post)
        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}

@Composable
fun PostItem(
  post: Post,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(shape = RoundedCornerShape(16.dp))
      .fillMaxWidth()
      .border(
        width = 1.dp,
        color = MaterialTheme.colors.onSurface,
        shape = RoundedCornerShape(16.dp)
      )
  ) {
    Text(
      text = post.text,
      modifier = Modifier.padding(16.dp)
    )
  }
}

@Preview
@Composable
private fun PostsListPreview() {
  val posts = (0..100).map { index ->
    Post("$index", "user$index", "This is a post number $index", index.toLong())
  }
  PostsList(posts)
}
