package nl.jovmit.friends.signup

import kotlinx.coroutines.runBlocking
import nl.jovmit.friends.domain.user.InMemoryUserDataStore
import nl.jovmit.friends.domain.user.OfflineUserCatalog
import nl.jovmit.friends.domain.user.UnavailableUserCatalog
import nl.jovmit.friends.domain.user.UserRepository
import nl.jovmit.friends.signup.state.SignUpState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FailedAccountCreationTest {

  @Test
  fun backendError() = runBlocking {
    val userRepository = UserRepository(UnavailableUserCatalog(), InMemoryUserDataStore())

    val result = userRepository.signUp(":email:", ":password:", ":about:")

    assertEquals(SignUpState.BackendError, result)
  }

  @Test
  fun offlineError() = runBlocking {
    val userRepository = UserRepository(OfflineUserCatalog(), InMemoryUserDataStore())

    val result = userRepository.signUp(":email:", ":password:", ":about:")

    assertEquals(SignUpState.Offline, result)
  }
}