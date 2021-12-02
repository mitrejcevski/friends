package nl.jovmit.friends

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import nl.jovmit.friends.home.HomeScreen
import nl.jovmit.friends.navigation.Screen
import nl.jovmit.friends.signup.SignUpScreen
import nl.jovmit.friends.ui.theme.FriendsTheme

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      FriendsTheme {
        Surface(color = MaterialTheme.colors.background) {
          MainApp()
        }
      }
    }
  }

  @Composable
  private fun MainApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.SignUp.route) {
      composable(Screen.SignUp.route) {
        SignUpScreen { signedUpUserId ->
          navController.navigate(Screen.Home.createRoute(signedUpUserId)) {
            popUpTo(Screen.SignUp.route) { inclusive = true }
          }
        }
      }
      composable(route = Screen.Home.route) { backStackEntry ->
        HomeScreen(userId = backStackEntry.arguments?.getString("userId") ?: "")
      }
    }
  }
}