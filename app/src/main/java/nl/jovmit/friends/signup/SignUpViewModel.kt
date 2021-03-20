package nl.jovmit.friends.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.jovmit.friends.signup.state.SignUpState
import java.util.regex.Pattern

class SignUpViewModel {

  private val _mutableSignUpState = MutableLiveData<SignUpState>()
  val signUpState: LiveData<SignUpState> = _mutableSignUpState

  fun createAccount(
    email: String,
    password: String,
    about: String
  ) {
    when (RegexCredentialsValidator().validate(email, password)) {
      is CredentialsValidationResult.InvalidEmail ->
        _mutableSignUpState.value = SignUpState.BadEmail
      is CredentialsValidationResult.InvalidPassword ->
        _mutableSignUpState.value = SignUpState.BadPassword
    }
  }

  class RegexCredentialsValidator {
    fun validate(
      email: String,
      password: String
    ): CredentialsValidationResult {
      val emailRegex =
        """[a-zA-Z0-9+._%\-]{1,64}@[a-zA-Z0-9][a-zA-Z0-9\-]{1,64}(\.[a-zA-Z0-9][a-zA-Z0-9\-]{1,25})"""
      val emailPattern = Pattern.compile(emailRegex)
      val passwordRegex = """^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*+=\-]).{8,}$"""
      val passwordPattern = Pattern.compile(passwordRegex)

      val result = if (!emailPattern.matcher(email).matches()) {
        CredentialsValidationResult.InvalidEmail
      } else if (!passwordPattern.matcher(password).matches()) {
        CredentialsValidationResult.InvalidPassword
      } else TODO()
      return result
    }
  }

  sealed class CredentialsValidationResult {

    object InvalidEmail : CredentialsValidationResult()

    object InvalidPassword : CredentialsValidationResult()
  }
}
