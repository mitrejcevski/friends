package nl.jovmit.friends.signup.state

import nl.jovmit.friends.domain.user.User

sealed class SignUpState {

  object Loading : SignUpState()

  data class SignedUp(val user: User) : SignUpState()

  object BadEmail : SignUpState()

  object BadPassword : SignUpState()

  object DuplicateAccount : SignUpState()

  object BackendError : SignUpState()

  object Offline : SignUpState()
}
