package nl.jovmit.friends.signup

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.app.TestDispatchers
import nl.jovmit.friends.domain.user.InMemoryUserCatalog
import nl.jovmit.friends.domain.user.User
import nl.jovmit.friends.domain.user.UserRepository
import nl.jovmit.friends.domain.validation.RegexCredentialsValidator
import nl.jovmit.friends.signup.state.SignUpState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class CreateAnAccountTest {

  private val credentialsValidator = RegexCredentialsValidator()
  private val viewModel = SignUpViewModel(
    credentialsValidator,
    UserRepository(InMemoryUserCatalog()),
    TestDispatchers()
  )

  @Test
  fun accountCreated() {
    val maya = User("mayaId", "maya@friends.com", "about Maya")

    viewModel.createAccount(maya.email, "MaY@2021", maya.about)

    assertEquals(SignUpState.SignedUp(maya), viewModel.signUpState.value)
  }

  @Test
  fun anotherAccountCreated() {
    val bob = User("bobId", "bob@friends.com", "about Bob")

    viewModel.createAccount(bob.email, "Ple@seSubscribe1", bob.about)

    assertEquals(SignUpState.SignedUp(bob), viewModel.signUpState.value)
  }

  @Test
  fun createDuplicateAccount() {
    val anna = User("annaId", "anna@friends.com", "about Anna")
    val password = "AnNaPas$123"
    val usersForEmail = mutableMapOf(anna.email to anna)
    val userRepository = UserRepository(InMemoryUserCatalog(usersForEmail))
    val viewModel = SignUpViewModel(credentialsValidator, userRepository, TestDispatchers())

    viewModel.createAccount(anna.email, password, anna.about)

    assertEquals(SignUpState.DuplicateAccount, viewModel.signUpState.value)
  }
}