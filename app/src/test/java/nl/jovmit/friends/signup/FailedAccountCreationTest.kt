package nl.jovmit.friends.signup

import nl.jovmit.friends.domain.exceptions.BackendException
import nl.jovmit.friends.domain.user.User
import nl.jovmit.friends.domain.user.UserCatalog
import nl.jovmit.friends.domain.user.UserRepository
import nl.jovmit.friends.signup.state.SignUpState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FailedAccountCreationTest {

  @Test
  fun backendError() {
    val userRepository = UserRepository(UnavailableUserCatalog())

    val result = userRepository.signUp("email", "password", "about")

    assertEquals(SignUpState.BackendError, result)
  }

  class UnavailableUserCatalog : UserCatalog {

    override fun createUser(email: String, password: String, about: String): User {
      throw BackendException()
    }
  }
}