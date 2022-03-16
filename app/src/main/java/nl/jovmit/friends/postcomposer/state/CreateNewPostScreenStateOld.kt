package nl.jovmit.friends.postcomposer.state

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class CreateNewPostScreenStateOld {

  var isLoading by mutableStateOf(false)
  var currentMessage by mutableStateOf(0)
  var isPostSubmitted by mutableStateOf(false)

  fun setPostSubmitted() {
    isPostSubmitted = true
  }

  fun showMessage(@StringRes message: Int) {
    isLoading = false
    if (currentMessage != message) {
      currentMessage = message
    }
  }

  fun showLoading() {
    isLoading = true
  }
}