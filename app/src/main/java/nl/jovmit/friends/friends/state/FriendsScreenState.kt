package nl.jovmit.friends.friends.state

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import nl.jovmit.friends.domain.user.Friend

@Parcelize
data class FriendsScreenState(
  val isLoading: Boolean = false,
  val friends: List<Friend> = emptyList(),
  val currentlyUpdatingFriends: List<String> = emptyList(),
  @StringRes val error: Int = 0
) : Parcelable