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

    viewModel.createAccount("maya@friends.com", "MaY@2021", "about Maya")

    assertEquals(SignUpState.SignedUp(maya), viewModel.signUpState.value)
  }
}