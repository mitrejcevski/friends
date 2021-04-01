package nl.jovmit.friends

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import nl.jovmit.friends.signup.SignUp
import nl.jovmit.friends.ui.theme.FriendsTheme

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val navController = rememberNavController()
      FriendsTheme {
        Surface(color = MaterialTheme.colors.background) {
          NavHost(navController = navController, startDestination = "signUp") {
            composable("signUp") {
              SignUp(onSignedUp = { navController.navigate("timeline") })
            }
            composable("timeline") {
              Text(text = stringResource(id = R.string.timeline))
            }
          }
        }
      }
    }
  }
}