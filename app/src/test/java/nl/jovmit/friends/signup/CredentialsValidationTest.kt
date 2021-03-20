package nl.jovmit.friends.signup

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.signup.state.SignUpState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class CredentialsValidationTest {

  @Test
  fun invalidEmail() {
    val viewModel = SignUpViewModel()

    viewModel.createAccount("email", ":password:", ":about:")

    assertEquals(SignUpState.BadEmail, viewModel.signUpState.value)
  }
}