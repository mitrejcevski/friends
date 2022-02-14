package nl.jovmit.friends.domain.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
  val id: String,
  val email: String,
  val about: String
) : Parcelable
