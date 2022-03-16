package nl.jovmit.friends.timeline

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.jovmit.friends.R
import nl.jovmit.friends.domain.post.Post
import nl.jovmit.friends.timeline.state.TimelineScreenState
import nl.jovmit.friends.ui.composables.BlockingLoading
import nl.jovmit.friends.ui.composables.InfoMessage
import nl.jovmit.friends.ui.composables.ScreenTitle
import nl.jovmit.friends.ui.extensions.toDateTime
import org.koin.androidx.compose.getViewModel

@Composable
fun TimelineScreen(
  userId: String,
  onCreateNewPost: () -> Unit
) {

  val timelineViewModel = getViewModel<TimelineViewModel>()
  var loadedUserId by remember { mutableStateOf("") }
  val screenState = timelineViewModel.timelineScreenState.observeAsState().value ?: TimelineScreenState()

  if (loadedUserId != userId) {
    loadedUserId = userId
    timelineViewModel.timelineFor(loadedUserId)
  }
  TimelineScreenContent(
    screenState = screenState,
    onCreateNewPost = { onCreateNewPost() }
  )
}

@Composable
private fun TimelineScreenContent(
  screenState: TimelineScreenState,
  onCreateNewPost: () -> Unit
) {
  Box {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
      ScreenTitle(resource = R.string.timeline)
      Spacer(modifier = Modifier.height(16.dp))
      Box(modifier = Modifier.fillMaxSize()) {
        PostsList(
          posts = screenState.posts,
          modifier = Modifier.align(Alignment.TopCenter)
        )
        FloatingActionButton(
          onClick = { onCreateNewPost() },
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .testTag(stringResource(id = R.string.createNewPost))
        ) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(id = R.string.createNewPost)
          )
        }
      }
    }
    InfoMessage(stringResource = screenState.error)
    BlockingLoading(isShowing = screenState.isLoading)
  }
}

@Composable
private fun PostsList(
  posts: List<Post>,
  modifier: Modifier = Modifier
) {
  if (posts.isEmpty()) {
    Text(
      text = stringResource(id = R.string.emptyTimelineMessage),
      modifier = modifier
    )
  } else {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
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
    Column(
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(text = post.userId)
        Text(text = post.timestamp.toDateTime())
      }
      Text(
        text = post.text,
        style = MaterialTheme.typography.h5,
        modifier = Modifier.padding(16.dp)
      )
    }
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
