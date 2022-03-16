package nl.jovmit.friends.postcomposer.state

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateNewPostScreenState(
  val isLoading: Boolean = false,
  val postText: String = "",
  val createdPostId: String = "",
  @StringRes val error: Int = 0
) : Parcelable