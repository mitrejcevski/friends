package nl.jovmit.friends.postcomposer

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import nl.jovmit.friends.MainActivity
import nl.jovmit.friends.domain.post.InMemoryPostCatalog
import nl.jovmit.friends.domain.post.PostCatalog
import nl.jovmit.friends.domain.user.InMemoryUserData
import nl.jovmit.friends.infrastructure.ControllableClock
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import java.time.LocalDateTime
import java.time.ZoneOffset

class CreateNewPostScreenTest {

  @get:Rule
  val createNewPostRule = createAndroidComposeRule<MainActivity>()

  @Test
  fun createNewPost() {
    val timestampWithTimezoneOffset = LocalDateTime
      .of(2021, 10, 30, 13, 30)
      .toInstant(ZoneOffset.ofTotalSeconds(0))
      .toEpochMilli()
    replaceUserDataWith(InMemoryUserData("jovmitId"))
    replacePostCatalogWith(InMemoryPostCatalog(clock = ControllableClock(timestampWithTimezoneOffset)))

    launchPostComposerFor("jovmit@friends.com", createNewPostRule) {
      typePost("My New Post")
      submit()
    } verify {
      newlyCreatedPostIsShown("jovmitId", "30-10-2021 15:30", "My New Post")
    }
  }

  @After
  fun tearDown() {
    replacePostCatalogWith(InMemoryPostCatalog())
    replaceUserDataWith(InMemoryUserData(""))
  }

  private fun replacePostCatalogWith(postCatalog: PostCatalog) {
    val module = module {
      single(override = true) { postCatalog }
    }
    loadKoinModules(module)
  }

  private fun replaceUserDataWith(userData: InMemoryUserData) {
    val module = module {
      single(override = true) { userData }
    }
    loadKoinModules(module)
  }
}

