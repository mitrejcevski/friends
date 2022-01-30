package nl.jovmit.friends.friends

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import nl.jovmit.friends.R
import nl.jovmit.friends.domain.user.Friend
import nl.jovmit.friends.friends.state.FriendsState
import nl.jovmit.friends.ui.composables.ScreenTitle
import org.koin.androidx.compose.getViewModel

@Composable
fun FriendsScreen(
  userId: String
) {

  val friendsViewModel = getViewModel<FriendsViewModel>()
  if (friendsViewModel.friendsState.value == null) {
    friendsViewModel.loadFriends(userId)
  }

  val friendsState = friendsViewModel.friendsState.observeAsState().value

  Box {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
      ScreenTitle(resource = R.string.friends)
      Spacer(modifier = Modifier.height(16.dp))
      if (friendsState is FriendsState.Loaded) {
        FriendsList(
          friends = friendsState.friends
        )
      }
    }
  }
}

@Composable
private fun FriendsList(
  friends: List<Friend>
) {
  if (friends.isEmpty()) {
    Text(
      text = stringResource(id = R.string.emptyFriendsMessage),
      modifier = Modifier.fillMaxWidth(),
      textAlign = TextAlign.Center
    )
  } else {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
      items(friends) { friend ->
        Text(text = friend.user.id)
      }
    }
  }
}
