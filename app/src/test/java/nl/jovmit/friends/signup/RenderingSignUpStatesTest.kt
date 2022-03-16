package nl.jovmit.friends.signup

import androidx.lifecycle.SavedStateHandle
import nl.jovmit.friends.InstantTaskExecutorExtension
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
class RenderingSignUpStatesTest {

  private val userRepository = UserRepository(InMemoryUserCatalog(), InMemoryUserDataStore())
  private val viewModel = SignUpViewModel(
    RegexCredentialsValidator(),
    userRepository,
    SavedStateHandle(),
    TestDispatchers()
  )
  private val tom = User("tomId", "tom@friends.com", "about Tom")

  @Test
  fun uiStatesAreDeliveredInParticularOrder() {
    val deliveredStates = mutableListOf<SignUpScreenState>()
    viewModel.screenState.observeForever { deliveredStates.add(it) }

    viewModel.createAccount(tom.email, "P@ssWord1#$", tom.about)

    assertEquals(
      listOf(SignUpScreenState(isLoading = true), SignUpScreenState(signedUpUserId = tom.id)),
      deliveredStates
    )
  }
}