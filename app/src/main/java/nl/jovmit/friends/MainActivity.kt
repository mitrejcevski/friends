package nl.jovmit.friends

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import nl.jovmit.friends.signup.SignUp
import nl.jovmit.friends.ui.theme.FriendsTheme

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      FriendsTheme {
        Surface(color = MaterialTheme.colors.background) {
          SignUp()
        }
      }
    }
  }
}