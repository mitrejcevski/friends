package nl.jovmit.friends.friends

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import nl.jovmit.friends.MainActivity
import nl.jovmit.friends.R
import nl.jovmit.friends.timeline.launchTimelineFor

private typealias MainActivityRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>

fun launchTimeline(
  rule: MainActivityRule,
  block: FriendsRobot.() -> Unit
): FriendsRobot {
  launchTimelineFor("email@email.com", "Pas$123.", rule) {}
  return FriendsRobot(rule).apply(block)
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

  fun friendsScreenIsPresent() {
    val friends = rule.activity.getString(R.string.friends)
    rule.onAllNodesWithText(friends)
      .onFirst()
      .assertIsDisplayed()
  }
}