package nl.jovmit.friends

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import nl.jovmit.friends.signup.SignUpScreen
import nl.jovmit.friends.signup.SignUpViewModel
import nl.jovmit.friends.timeline.TimelineScreen
import nl.jovmit.friends.timeline.TimelineViewModel
import nl.jovmit.friends.ui.theme.FriendsTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

  private val signUpViewModel: SignUpViewModel by viewModel()
  private val timelineViewModel: TimelineViewModel by viewModel()

  private companion object {
    private const val SIGN_UP = "signUp"
    private const val TIMELINE = "timeline"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val navController = rememberNavController()
      FriendsTheme {
        Surface(color = MaterialTheme.colors.background) {
          NavHost(navController = navController, startDestination = SIGN_UP) {
            composable(SIGN_UP) {
              SignUpScreen(signUpViewModel) { signedUpUserId ->
                navController.navigate("$TIMELINE/$signedUpUserId") {
                  popUpTo(SIGN_UP) { inclusive = true }
                }
              }
            }
            composable(
              route = "$TIMELINE/{userId}",
              arguments = listOf(navArgument("userId") { })
            ) { backStackEntry ->
              TimelineScreen(
                backStackEntry.arguments?.getString("userId") ?: "",
                timelineViewModel
              )
            }
          }
        }
      }
    }
  }
}