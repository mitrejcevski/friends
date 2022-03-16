package nl.jovmit.friends.signup

import androidx.lifecycle.SavedStateHandle
import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.R
import nl.jovmit.friends.app.TestDispatchers
import nl.jovmit.friends.domain.user.InMemoryUserCatalog
import nl.jovmit.friends.domain.user.InMemoryUserDataStore
import nl.jovmit.friends.domain.user.User
import nl.jovmit.friends.domain.user.UserRepository
import nl.jovmit.friends.domain.validation.RegexCredentialsValidator
import nl.jovmit.friends.signup.state.SignUpScreenState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class CreateAnAccountTest {

  private val credentialsValidator = RegexCredentialsValidator()
  private val stateHandle = SavedStateHandle()
  private val dispatchers = TestDispatchers()
  private val viewModel = SignUpViewModel(
    credentialsValidator,
    UserRepository(InMemoryUserCatalog(), InMemoryUserDataStore()),
    stateHandle,
    dispatchers
  )

  @Test
  fun accountCreated() {
    val maya = User("mayaId", "maya@friends.com", "about Maya")

    viewModel.createAccount(maya.email, "MaY@2021", maya.about)

    assertEquals(SignUpScreenState(signedUpUserId = maya.id), viewModel.screenState.value)
  }

  @Test
  fun anotherAccountCreated() {
    val bob = User("bobId", "bob@friends.com", "about Bob")

    viewModel.createAccount(bob.email, "Ple@seSubscribe1", bob.about)

    assertEquals(SignUpScreenState(signedUpUserId = bob.id), viewModel.screenState.value)
  }

  @Test
  fun createDuplicateAccount() {
    val anna = User("annaId", "anna@friends.com", "about Anna")
    val password = "AnNaPas$123"
    val usersForPassword = mutableMapOf(password to mutableListOf(anna))
    val userRepository = UserRepository(
      InMemoryUserCatalog(usersForPassword),
      InMemoryUserDataStore()
    )
    val viewModel = SignUpViewModel(credentialsValidator, userRepository, stateHandle, dispatchers)

    viewModel.createAccount(anna.email, password, anna.about)

    assertEquals(
      SignUpScreenState(error = R.string.duplicateAccountError),
      viewModel.screenState.value
    )
  }
}