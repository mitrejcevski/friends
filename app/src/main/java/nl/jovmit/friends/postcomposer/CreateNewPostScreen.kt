package nl.jovmit.friends.postcomposer

import androidx.compose.runtime.Composable
import nl.jovmit.friends.R
import nl.jovmit.friends.ui.composables.ScreenTitle

@Composable
fun CreateNewPostScreen() {
  ScreenTitle(resource = R.string.createNewPost)
}