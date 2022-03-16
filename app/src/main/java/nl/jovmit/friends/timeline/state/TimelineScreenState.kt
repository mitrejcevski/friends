package nl.jovmit.friends.timeline.state

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import nl.jovmit.friends.domain.post.Post

@Parcelize
data class TimelineScreenState(
  val isLoading: Boolean = false,
  val posts: List<Post> = emptyList(),
  @StringRes val error: Int = 0
) : Parcelable