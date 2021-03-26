package nl.jovmit.friends.signup

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.domain.user.User
import nl.jovmit.friends.domain.validation.RegexCredentialsValidator
import nl.jovmit.friends.signup.state.SignUpState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class CreateAnAccountTest {

  @Test
  fun accountCreated() {
    val maya = User("mayaId", "maya@friends.com", "about Maya")
    val viewModel = SignUpViewModel(RegexCredentialsValidator())

    viewModel.createAccount(maya.email, "MaY@2021", maya.about)

    assertEquals(SignUpState.SignedUp(maya), viewModel.signUpState.value)
  }

  @Test
  fun anotherAccountCreated() {
    val bob = User("bobId", "bob@friends.com", "about Bob")
    val viewModel = SignUpViewModel(RegexCredentialsValidator())

    viewModel.createAccount(bob.email, "Ple@seSubscribe1", bob.about)

    assertEquals(SignUpState.SignedUp(bob), viewModel.signUpState.value)
  }

  @Test
  fun createDuplicateAccount() {
    val anna = User("annaId", "anna@friends.com", "about Anna")
    val password = "AnNaPas$123"
    val viewModel = SignUpViewModel(RegexCredentialsValidator()).also {
      it.createAccount(anna.email, password, anna.about)
    }

    viewModel.createAccount(anna.email, password, anna.about)

    assertEquals(SignUpState.DuplicateAccount, viewModel.signUpState.value)
  }
}