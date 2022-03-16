package nl.jovmit.friends.postcomposer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import nl.jovmit.friends.R
import nl.jovmit.friends.postcomposer.state.CreateNewPostScreenState
import nl.jovmit.friends.ui.composables.BlockingLoading
import nl.jovmit.friends.ui.composables.InfoMessage
import nl.jovmit.friends.ui.composables.ScreenTitle
import org.koin.androidx.compose.getViewModel

@Composable
fun CreateNewPostScreen(
  onPostCreated: () -> Unit
) {
  val viewModel = getViewModel<CreatePostViewModel>()
  val createPostState = viewModel.screenState.observeAsState().value ?: CreateNewPostScreenState()

  if (createPostState.createdPostId.isNotBlank()) {
    onPostCreated()
  }
  CreateNewPostScreenContent(
    screenState = createPostState,
    onPostTextUpdated = viewModel::updatePostText,
    onSubmitPost = { viewModel.createPost(it) }
  )
}

@Composable
private fun CreateNewPostScreenContent(
  screenState: CreateNewPostScreenState,
  onPostTextUpdated: (String) -> Unit,
  onSubmitPost: (String) -> Unit
) {
  Box {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
      ScreenTitle(resource = R.string.createNewPost)
      Spacer(modifier = Modifier.height(16.dp))
      Box(modifier = Modifier.fillMaxSize()) {
        PostComposer(
          postText = screenState.postText,
          onValueChange = { onPostTextUpdated(it) },
          onDone = { onSubmitPost(screenState.postText) }
        )
        FloatingActionButton(
          onClick = { onSubmitPost(screenState.postText) },
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .testTag(stringResource(id = R.string.submitPost))
        ) {
          Icon(
            imageVector = Icons.Default.Done,
            contentDescription = stringResource(id = R.string.submitPost)
          )
        }
      }
    }
    InfoMessage(stringResource = screenState.error)
    BlockingLoading(isShowing = screenState.isLoading)
  }
}

@Composable
private fun PostComposer(
  postText: String,
  onValueChange: (String) -> Unit,
  onDone: () -> Unit
) {
  OutlinedTextField(
    value = postText,
    onValueChange = onValueChange,
    modifier = Modifier.fillMaxWidth(),
    label = { Text(text = stringResource(id = R.string.newPostHint)) },
    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    keyboardActions = KeyboardActions(onDone = { onDone() })
  )
}
