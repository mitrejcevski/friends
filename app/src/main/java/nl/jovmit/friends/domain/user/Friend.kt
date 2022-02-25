package nl.jovmit.friends.domain.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Friend(
  val user: User,
  val isFollowee: Boolean
) : Parcelable