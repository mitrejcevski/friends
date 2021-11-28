package nl.jovmit.friends.people

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import nl.jovmit.friends.MainActivity
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

class PeopleScreenTest {

  @get:Rule
  val rule = createAndroidComposeRule<MainActivity>()

  @Test
  @Ignore("In Development")
  fun displaysPeople() {
    launchPeopleFor("email@email.com", rule) {
      tapOnPeople()
    } verify {
      peopleScreenIsPresent()
    }
  }
}