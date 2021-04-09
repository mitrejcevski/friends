package nl.jovmit.friends

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import nl.jovmit.friends.signup.SignUpScreen
import nl.jovmit.friends.timeline.Timeline
import nl.jovmit.friends.ui.theme.FriendsTheme

class MainActivity : ComponentActivity() {

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
              SignUpScreen(onSignedUp = { navController.navigate(TIMELINE) })
            }
            composable(TIMELINE) {
              Timeline()
            }
          }
        }
      }
    }
  }
}