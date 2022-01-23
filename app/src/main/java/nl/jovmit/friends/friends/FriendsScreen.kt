package nl.jovmit.friends.friends

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import nl.jovmit.friends.R
import nl.jovmit.friends.ui.composables.ScreenTitle
import org.koin.androidx.compose.getViewModel

@Composable
fun FriendsScreen(
  userId: String
) {

  val friendsViewModel = getViewModel<FriendsViewModel>()
  friendsViewModel.loadFriends(userId)

  Box {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
      ScreenTitle(resource = R.string.friends)
      Spacer(modifier = Modifier.height(16.dp))
      Text(text = stringResource(id = R.string.emptyFriendsMessage))
    }
  }
}