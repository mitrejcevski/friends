package nl.jovmit.friends.timeline

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import nl.jovmit.friends.MainActivity
import nl.jovmit.friends.R
import nl.jovmit.friends.domain.post.Post
import nl.jovmit.friends.signup.launchSignUpScreen

typealias MainActivityRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>

fun launchTimelineFor(
  email: String,
  password: String,
  timelineTestRule: MainActivityRule,
  block: TimelineRobot.() -> Unit
): TimelineRobot {
  launchSignUpScreen(timelineTestRule) {
    typeEmail(email)
    typePassword(password)
    submit()
  }
  return TimelineRobot(timelineTestRule).apply(block)
}

class TimelineRobot(
  private val rule: MainActivityRule
) {

  fun tapOnCreateNewPost() {
    val createNewPost = rule.activity.getString(R.string.createNewPost)
    rule.onNodeWithTag(createNewPost)
      .performClick()
  }

  fun tapOnFriendsTab() {
    val friends = rule.activity.getString(R.string.friends)
    rule.onNodeWithText(friends)
      .performClick()
  }

  infix fun verify(
    block: TimelineVerificationRobot.() -> Unit
  ): TimelineVerificationRobot {
    return TimelineVerificationRobot(rule).apply(block)
  }
}

class TimelineVerificationRobot(
  private val rule: MainActivityRule
) {

  fun emptyTimelineMessageIsDisplayed() {
    val emptyTimelineMessage = rule.activity.getString(R.string.emptyTimelineMessage)
    rule.onNodeWithText(emptyTimelineMessage)
      .assertIsDisplayed()
  }

  fun postsAreDisplayed(vararg posts: Post) {
    posts.forEach { post ->
      rule.onNodeWithText(post.text)
        .assertIsDisplayed()
    }
  }

  fun newPostComposerIsDisplayed() {
    val createNewPost = rule.activity.getString(R.string.createNewPost)
    rule.onNodeWithText(createNewPost)
      .assertIsDisplayed()
  }

  fun loadingIndicatorIsDisplayed() {
    val loading = rule.activity.getString(R.string.loading)
    rule.onNodeWithTag(loading)
      .assertIsDisplayed()
  }

  fun backendErrorIsDisplayed() {
    val errorMessage = rule.activity.getString(R.string.fetchingTimelineError)
    rule.onNodeWithText(errorMessage)
      .assertIsDisplayed()
  }

  fun offlineErrorIsDisplayed() {
    val offline = rule.activity.getString(R.string.offlineError)
    rule.onNodeWithText(offline)
      .assertIsDisplayed()
  }

  fun friendsScreenIsDisplayed() {
    val friends = rule.activity.getString(R.string.friends)
    rule.onAllNodesWithText(friends)
      .onFirst()
      .assertIsDisplayed()
  }
}
