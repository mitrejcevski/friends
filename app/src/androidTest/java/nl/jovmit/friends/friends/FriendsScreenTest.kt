package nl.jovmit.friends.friends

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import nl.jovmit.friends.MainActivity
import org.junit.Rule
import org.junit.Test

class FriendsScreenTest {

  @get:Rule
  val rule = createAndroidComposeRule<MainActivity>()

  @Test
  fun showsEmptyFriendsMessage() {
    launchFriends(rule) {
      //no operation
    } verify {
      emptyFriendsMessageIsDisplayed()
    }
  }
}