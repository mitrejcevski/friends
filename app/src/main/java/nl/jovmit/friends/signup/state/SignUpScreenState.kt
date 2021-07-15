package nl.jovmit.friends.signup.state

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class SignUpScreenState {
  var email by mutableStateOf("")
  var isBadEmail by mutableStateOf(false)
  var password by mutableStateOf("")
  var isBadPassword by mutableStateOf(false)
  var about by mutableStateOf("")
  var currentInfoMessage by mutableStateOf(0)
  var isLoading by mutableStateOf(false)

  private var lastSubmittedEmail by mutableStateOf("")
  private var lastSubmittedPassword by mutableStateOf("")

  val showBadEmail: Boolean
    get() = isBadEmail && lastSubmittedEmail == email

  val showBadPassword: Boolean
    get() = isBadPassword && lastSubmittedPassword == password

  fun showBadEmail() {
    isBadEmail = true
  }

  fun showBadPassword() {
    isBadPassword = true
  }

  fun toggleInfoMessage(@StringRes message: Int) {
    isLoading = false
    if (currentInfoMessage != message) {
      currentInfoMessage = message
    }
  }

  fun toggleLoading() {
    isLoading = true
  }

  fun resetUiState() {
    currentInfoMessage = 0
    lastSubmittedEmail = email
    lastSubmittedPassword = password
    isBadEmail = false
    isBadPassword = false
    isLoading = false
  }
}