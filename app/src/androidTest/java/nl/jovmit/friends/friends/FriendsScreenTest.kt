package nl.jovmit.friends.friends

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import nl.jovmit.friends.MainActivity
import nl.jovmit.friends.domain.user.Friend
import nl.jovmit.friends.domain.user.InMemoryUserCatalog
import nl.jovmit.friends.domain.user.User
import nl.jovmit.friends.domain.user.UserCatalog
import org.junit.After
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class FriendsScreenTest {

  @get:Rule
  val rule = createAndroidComposeRule<MainActivity>()

  @Test
  @Ignore("Waiting for the BL update")
  fun showsEmptyFriendsMessage() {
    launchFriends(rule) {
      //no operation
    } verify {
      emptyFriendsMessageIsDisplayed()
    }
  }

  @Test
  fun showsAvailableFriends() {
    val ana = User("anaId", "ana@friends.com", "")
    val friendAna = Friend(ana, false)
    val bob = User("bobId", "bob@friends.com", "")
    val friendBob = Friend(bob, false)
    val users = mutableMapOf("" to mutableListOf(ana, bob))
    replaceUserCatalogWith(InMemoryUserCatalog(users))

    launchFriends(rule) {
      //no operation
    } verify {
      friendsAreDisplayed(friendAna, friendBob)
    }
  }

  @After
  fun tearDown() {
    replaceUserCatalogWith(InMemoryUserCatalog())
  }

  private fun replaceUserCatalogWith(userCatalog: UserCatalog) {
    val replaceModule = module {
      factory { userCatalog }
    }
    loadKoinModules(replaceModule)
  }
}