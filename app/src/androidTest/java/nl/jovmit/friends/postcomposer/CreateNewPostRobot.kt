package nl.jovmit.friends.postcomposer

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import nl.jovmit.friends.MainActivity
import nl.jovmit.friends.R
import nl.jovmit.friends.timeline.launchTimelineFor

private typealias MainActivityRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>

fun launchPostComposerFor(
  email: String,
  createNewPostRule: MainActivityRule,
  block: CreateNewPostRobot.() -> Unit
): CreateNewPostRobot {
  launchTimelineFor(email, "sOmEPas$123", createNewPostRule) {
    tapOnCreateNewPost()
  }
  return CreateNewPostRobot(createNewPostRule).apply(block)
}

class CreateNewPostRobot(
  private val rule: MainActivityRule
) {

  fun typePost(postContent: String) {
    val newPostHint = rule.activity.getString(R.string.newPostHint)
    rule.onNodeWithText(newPostHint)
      .performTextInput(postContent)
  }

  fun submit() {
    val submitPost = rule.activity.getString(R.string.submitPost)
    rule.onNodeWithTag(submitPost)
      .performClick()
  }

  fun tapOnCreateNewPost() {
    val createNewPost = rule.activity.getString(R.string.createNewPost)
    rule.onNodeWithTag(createNewPost)
      .performClick()
  }

  infix fun verify(
    block: CreateNewPostVerificationRobot.() -> Unit
  ): CreateNewPostVerificationRobot {
    return CreateNewPostVerificationRobot(rule).apply(block)
  }
}

class CreateNewPostVerificationRobot(
  private val rule: MainActivityRule
) {

  fun newlyCreatedPostIsShown(
    userId: String,
    dateTime: String,
    postContent: String
  ) {
    rule.onAllNodesWithText(userId).onFirst().assertIsDisplayed()
    rule.onAllNodesWithText(userId).onLast().assertIsDisplayed()
    rule.onAllNodesWithText(dateTime).onFirst().assertIsDisplayed()
    rule.onAllNodesWithText(dateTime).onLast().assertIsDisplayed()
    rule.onNodeWithText(postContent).assertIsDisplayed()
  }
}
