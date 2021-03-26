package nl.jovmit.friends.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.jovmit.friends.domain.user.User
import nl.jovmit.friends.domain.validation.CredentialsValidationResult
import nl.jovmit.friends.domain.validation.RegexCredentialsValidator
import nl.jovmit.friends.signup.state.SignUpState

class SignUpViewModel(
  private val credentialsValidator: RegexCredentialsValidator
) {

  private val _mutableSignUpState = MutableLiveData<SignUpState>()
  val signUpState: LiveData<SignUpState> = _mutableSignUpState

  fun createAccount(
    email: String,
    password: String,
    about: String
  ) {
    when (credentialsValidator.validate(email, password)) {
      is CredentialsValidationResult.InvalidEmail ->
        _mutableSignUpState.value = SignUpState.BadEmail
      is CredentialsValidationResult.InvalidPassword ->
        _mutableSignUpState.value = SignUpState.BadPassword
      is CredentialsValidationResult.Valid -> {

        try {
          val user = createUser(email, password, about)
          _mutableSignUpState.value = SignUpState.SignedUp(user)
        } catch (duplicateAccount: DuplicateAccountException) {
          _mutableSignUpState.value = SignUpState.DuplicateAccount
        }
      }
    }
  }

  private fun createUser(
    email: String,
    password: String,
    about: String
  ): User {
    if (usersForPassword.values.flatten().any { it.email == email }) {
      throw DuplicateAccountException()
    }
    val userId = email.takeWhile { it != '@' } + "Id"
    val user = User(userId, email, about)
    usersForPassword.getOrPut(password, ::mutableListOf).add(user)
    return user
  }

  class DuplicateAccountException : Throwable()

  private val usersForPassword = mutableMapOf<String, MutableList<User>>()
}
