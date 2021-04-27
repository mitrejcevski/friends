package nl.jovmit.friends.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.jovmit.friends.domain.user.UserRepository
import nl.jovmit.friends.domain.validation.CredentialsValidationResult
import nl.jovmit.friends.domain.validation.RegexCredentialsValidator
import nl.jovmit.friends.signup.state.SignUpState

class SignUpViewModel(
  private val credentialsValidator: RegexCredentialsValidator,
  private val userRepository: UserRepository
) : ViewModel() {

  private val mutableSignUpState = MutableLiveData<SignUpState>()
  val signUpState: LiveData<SignUpState> = mutableSignUpState

  fun createAccount(
    email: String,
    password: String,
    about: String
  ) {
    when (credentialsValidator.validate(email, password)) {
      is CredentialsValidationResult.InvalidEmail ->
        mutableSignUpState.value = SignUpState.BadEmail
      is CredentialsValidationResult.InvalidPassword ->
        mutableSignUpState.value = SignUpState.BadPassword
      is CredentialsValidationResult.Valid ->
        proceedWithSignUp(email, password, about)
    }
  }

  private fun proceedWithSignUp(email: String, password: String, about: String) {
    mutableSignUpState.value = SignUpState.Loading
    mutableSignUpState.value = userRepository.signUp(email, password, about)
  }
}
