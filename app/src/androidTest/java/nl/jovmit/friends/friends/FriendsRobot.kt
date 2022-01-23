package nl.jovmit.friends.friends

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import nl.jovmit.friends.MainActivity
import nl.jovmit.friends.R
import nl.jovmit.friends.timeline.launchTimelineFor

private typealias MainActivityRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>

fun launchFriends(
  rule: MainActivityRule,
  block: FriendsRobot.() -> Unit
): FriendsRobot {
  launchTimelineFor("email@email.com", "Pas$123.", rule) {}
  return FriendsRobot(rule).apply {
    tapOnFriends()
    block()
  }
}

class FriendsRobot(private val rule: MainActivityRule) {

  fun tapOnFriends() {
    val friends = rule.activity.getString(R.string.friends)
    rule.onNodeWithText(friends)
      .performClick()
  }

  infix fun verify(
    block: FriendsVerificationRobot.() -> Unit
  ): FriendsVerificationRobot {
    return FriendsVerificationRobot(rule).apply(block)
  }
}

class FriendsVerificationRobot(
  private val rule: MainActivityRule
) {

  fun emptyFriendsMessageIsDisplayed() {
    val emptyFriendsMessage = rule.activity.getString(R.string.emptyFriendsMessage)
    rule.onNodeWithText(emptyFriendsMessage)
      .assertIsDisplayed()
  }
}