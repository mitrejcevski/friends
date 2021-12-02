package nl.jovmit.friends.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import nl.jovmit.friends.R

sealed class Screen(val route: String) {

  object SignUp : Screen("signUp")

  object Home : Screen("home/{userId}") {
    const val userId = "userId"
    fun createRoute(userId: String) = "home/$userId"
  }

  sealed class Main(
    route: String,
    @StringRes val title: Int,
    @DrawableRes val icon: Int
  ) : Screen(route) {

    object Timeline : Main("timeline", R.string.timeline, R.drawable.ic_timeline)

    object People : Main("people", R.string.people, R.drawable.ic_people)
  }

  object PostComposer : Screen("createNewPost")
}