package nl.jovmit.friends.friends

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import kotlinx.coroutines.delay
import nl.jovmit.friends.MainActivity
import nl.jovmit.friends.domain.user.Friend
import nl.jovmit.friends.domain.user.InMemoryUserCatalog
import nl.jovmit.friends.domain.user.User
import nl.jovmit.friends.domain.user.UserCatalog
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class FriendsScreenTest {

  @get:Rule
  val rule = createAndroidComposeRule<MainActivity>()

  private val ana = User("anaId", "ana@friends.com", "something about Anna")
  private val friendAna = Friend(ana, false)
  private val bob = User("bobId", "bob@friends.com", "")
  private val friendBob = Friend(bob, false)
  private val users = mutableMapOf("" to mutableListOf(ana, bob))

  @Test
  fun showsEmptyFriendsMessage() {
    launchFriends(rule) {
      //no operation
    } verify {
      emptyFriendsMessageIsDisplayed()
    }
  }

  @Test
  fun showsLoadingIndicator() {
    replaceUserCatalogWith(DelayingUserCatalog(listOf(friendAna, friendBob)))

    launchFriends(rule) {
      //no operation
    } verify {
      loadingIndicatorIsShown()
    }
  }

  @Test
  fun showsAvailableFriends() {
    replaceUserCatalogWith(InMemoryUserCatalog(users))

    launchFriends(rule) {
      //no operation
    } verify {
      friendsAreDisplayed(friendAna, friendBob)
    }
  }

  @Test
  fun showsRequiredFriendInformation() {
    val users = mutableMapOf("" to mutableListOf(ana))
    replaceUserCatalogWith(InMemoryUserCatalog(users))

    launchFriends(rule) {
      //no operation
    } verify {
      friendInformationIsShownFor(friendAna)
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

  private class DelayingUserCatalog(
    private val friends: List<Friend>
  ) : UserCatalog {

    override suspend fun createUser(email: String, password: String, about: String): User {
      return User(":irrelevant:", email, about)
    }

    override suspend fun followedBy(userId: String): List<String> {
      return emptyList()
    }

    override suspend fun loadFriendsFor(userId: String): List<Friend> {
      delay(1000)
      return friends
    }
  }
}