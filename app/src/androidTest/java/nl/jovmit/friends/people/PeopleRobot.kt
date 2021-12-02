package nl.jovmit.friends.people

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import nl.jovmit.friends.MainActivity
import nl.jovmit.friends.R
import nl.jovmit.friends.timeline.launchTimelineFor

private typealias MainActivityRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>

fun launchPeopleFor(
  email: String,
  rule: MainActivityRule,
  block: PeopleRobot.() -> Unit
): PeopleRobot {
  launchTimelineFor(email, "Pas$123.", rule) {}
  return PeopleRobot(rule).apply(block)
}

class PeopleRobot(private val rule: MainActivityRule) {

  fun tapOnPeople() {
    val people = rule.activity.getString(R.string.people)
    rule.onNodeWithText(people)
      .performClick()
  }

  infix fun verify(
    block: PeopleVerificationRobot.() -> Unit
  ): PeopleVerificationRobot {
    return PeopleVerificationRobot(rule).apply(block)
  }
}

class PeopleVerificationRobot(
  private val rule: MainActivityRule
) {

  fun peopleScreenIsPresent() {
    val people = rule.activity.getString(R.string.people)
    rule.onAllNodesWithText(people)
      .onFirst()
      .assertIsDisplayed()
  }
}