package nl.jovmit.friends.domain.user

import nl.jovmit.friends.domain.exceptions.BackendException
import nl.jovmit.friends.domain.exceptions.ConnectionUnavailableException
import nl.jovmit.friends.domain.exceptions.DuplicateAccountException
import nl.jovmit.friends.signup.state.SignUpState

class UserRepository(
  private val userCatalog: UserCatalog
) {

  suspend fun signUp(
    email: String,
    password: String,
    about: String
  ): SignUpState {
    return try {
      val user = userCatalog.createUser(email, password, about)
      SignUpState.SignedUp(user)
    } catch (duplicateAccount: DuplicateAccountException) {
      SignUpState.DuplicateAccount
    } catch (backendException: BackendException) {
      SignUpState.BackendError
    } catch (offlineException: ConnectionUnavailableException) {
      SignUpState.Offline
    }
  }
}