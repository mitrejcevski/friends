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
class RenderingSignUpStatesTest {

  private val userRepository = UserRepository(InMemoryUserCatalog())
  private val viewModel = SignUpViewModel(
    RegexCredentialsValidator(),
    userRepository,
    TestDispatchers()
  )
  private val tom = User("tomId", "tom@friends.com", "about Tom")

  @Test
  fun uiStatesAreDeliveredInParticularOrder() {
    val deliveredStates = mutableListOf<SignUpState>()
    viewModel.signUpState.observeForever { deliveredStates.add(it) }

    viewModel.createAccount(tom.email, "P@ssWord1#$", tom.about)

    assertEquals(
      listOf(SignUpState.Loading, SignUpState.SignedUp(tom)),
      deliveredStates
    )
  }
}