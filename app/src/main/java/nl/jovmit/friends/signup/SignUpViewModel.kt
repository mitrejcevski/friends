package nl.jovmit.friends.signup

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jovmit.friends.R
import nl.jovmit.friends.app.CoroutineDispatchers
import nl.jovmit.friends.domain.user.UserRepository
import nl.jovmit.friends.domain.validation.CredentialsValidationResult
import nl.jovmit.friends.domain.validation.RegexCredentialsValidator
import nl.jovmit.friends.signup.state.SignUpScreenState
import nl.jovmit.friends.signup.state.SignUpState

class SignUpViewModel(
  private val credentialsValidator: RegexCredentialsValidator,
  private val userRepository: UserRepository,
  private val savedStateHandle: SavedStateHandle,
  private val dispatchers: CoroutineDispatchers
) : ViewModel() {

  val screenState: LiveData<SignUpScreenState> =
    savedStateHandle.getLiveData(SCREEN_STATE_KEY)

  fun createAccount(email: String, password: String, about: String) {
    when (credentialsValidator.validate(email, password)) {
      is CredentialsValidationResult.InvalidEmail -> setBadEmail()
      is CredentialsValidationResult.InvalidPassword -> setBadPassword()
      is CredentialsValidationResult.Valid -> proceedWithSignUp(email, password, about)
    }
  }

  fun updateEmail(email: String) {
    val currentState = currentScreenState()
    updateScreenState(currentState.copy(email = email, isBadEmail = false))
  }

  fun updatePassword(password: String) {
    val currentState = currentScreenState()
    updateScreenState(currentState.copy(password = password, isBadPassword = false))
  }

  fun updateAbout(about: String) {
    val currentState = currentScreenState()
    updateScreenState(currentState.copy(about = about))
  }

  private fun proceedWithSignUp(email: String, password: String, about: String) {
    viewModelScope.launch {
      setLoading()
      val result = withContext(dispatchers.background) {
        userRepository.signUp(email, password, about)
      }
      updateScreenStateFor(result)
    }
  }

  private fun updateScreenStateFor(signUpState: SignUpState) {
    when (signUpState) {
      is SignUpState.SignedUp -> setSignedUp(signUpState.user.id)
      is SignUpState.BackendError -> setError(R.string.createAccountError)
      is SignUpState.Offline -> setError(R.string.offlineError)
      is SignUpState.DuplicateAccount -> setError(R.string.duplicateAccountError)
    }
  }

  private fun setLoading() {
    val currentState = currentScreenState()
    updateScreenState(currentState.copy(isLoading = true))
  }

  private fun setSignedUp(signedUpUserId: String) {
    val screenState = currentScreenState()
    updateScreenState(screenState.copy(isLoading = false, signedUpUserId = signedUpUserId))
  }

  private fun setBadEmail() {
    val screenState = currentScreenState()
    updateScreenState(screenState.copy(isBadEmail = true))
  }

  private fun setBadPassword() {
    val screenState = currentScreenState()
    updateScreenState(screenState.copy(isBadPassword = true))
  }

  private fun setError(@StringRes errorResource: Int) {
    val screenState = currentScreenState()
    updateScreenState(screenState.copy(isLoading = false, error = errorResource))
  }

  private fun currentScreenState(): SignUpScreenState {
    return savedStateHandle[SCREEN_STATE_KEY] ?: SignUpScreenState()
  }

  private fun updateScreenState(newState: SignUpScreenState) {
    savedStateHandle[SCREEN_STATE_KEY] = newState
  }

  private companion object {
    private const val SCREEN_STATE_KEY = "signUpScreenState"
  }
}
