package nl.jovmit.friends.postcomposer

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import nl.jovmit.friends.MainActivity
import nl.jovmit.friends.domain.user.InMemoryUserData
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class CreateNewPostScreenTest {

  @get:Rule
  val createNewPostRule = createAndroidComposeRule<MainActivity>()

  @Test
  @Ignore("Under Construction")
  fun createNewPost() {
    replaceUserDataWith(InMemoryUserData("jovmitId"))
    launchPostComposerFor("jovmit@friends.com", createNewPostRule) {
      typePost("My New Post")
      submit()
    } verify {
      newlyCreatedPostIsShown("jovmitId", "30-10-2021 15:30", "My New Post")
    }
  }

  private fun replaceUserDataWith(userData: InMemoryUserData) {
    val module = module {
      single(override = true) { userData }
    }
    loadKoinModules(module)
  }
}

