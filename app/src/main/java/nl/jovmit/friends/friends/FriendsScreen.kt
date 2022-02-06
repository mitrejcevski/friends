package nl.jovmit.friends.friends

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import nl.jovmit.friends.R
import nl.jovmit.friends.domain.user.Friend
import nl.jovmit.friends.friends.state.FriendsState
import nl.jovmit.friends.ui.composables.InfoMessage
import nl.jovmit.friends.ui.composables.ScreenTitle
import org.koin.androidx.compose.getViewModel

data class FriendsScreenState(
  val isLoading: Boolean = false,
  val friends: List<Friend> = emptyList(),
  @StringRes val error: Int = 0
)

@Composable
fun FriendsScreen(
  userId: String
) {

  val friendsViewModel = getViewModel<FriendsViewModel>()
  if (friendsViewModel.friendsState.value == null) {
    friendsViewModel.loadFriends(userId)
  }

  var screenState by remember { mutableStateOf(FriendsScreenState()) }
  val friendsState = friendsViewModel.friendsState.observeAsState().value
  if (friendsState is FriendsState.Loading) {
    screenState = screenState.copy(isLoading = true)
  }
  if (friendsState is FriendsState.Loaded) {
    screenState = screenState.copy(isLoading = false, friends = friendsState.friends)
  }
  if (friendsState is FriendsState.BackendError) {
    screenState = screenState.copy(isLoading = false, error = R.string.fetchingFriendsError)
  }

  Box {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
      ScreenTitle(resource = R.string.friends)
      Spacer(modifier = Modifier.height(16.dp))
      FriendsList(
        isRefreshing = screenState.isLoading,
        friends = screenState.friends,
        onRefresh = { friendsViewModel.loadFriends(userId) }
      )
    }
    InfoMessage(stringResource = screenState.error)
  }
}

@Composable
private fun FriendsList(
  modifier: Modifier = Modifier,
  isRefreshing: Boolean,
  friends: List<Friend>,
  onRefresh: () -> Unit
) {
  val loadingContentDescription = stringResource(id = R.string.loading)
  SwipeRefresh(
    modifier = modifier.semantics { contentDescription = loadingContentDescription },
    state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
    onRefresh = { onRefresh() }
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
          FriendItem(friend)
          Spacer(modifier = Modifier.height(16.dp))
        }
      }
    }
  }
}

@Composable
private fun FriendItem(
  friend: Friend,
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
    Row(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(Modifier.weight(1f)) {
        Text(text = friend.user.id)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = friend.user.about)
      }
      OutlinedButton(onClick = { /*TODO*/ }) {
        Text(text = stringResource(id = R.string.follow))
      }
    }
  }
}
