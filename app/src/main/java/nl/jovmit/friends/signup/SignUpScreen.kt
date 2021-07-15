package nl.jovmit.friends.signup

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import nl.jovmit.friends.R
import nl.jovmit.friends.signup.state.SignUpScreenState
import nl.jovmit.friends.signup.state.SignUpState
import nl.jovmit.friends.ui.composables.BlockingLoading
import nl.jovmit.friends.ui.composables.InfoMessage
import nl.jovmit.friends.ui.composables.ScreenTitle

@Composable
fun SignUpScreen(
  signUpViewModel: SignUpViewModel,
  onSignedUp: (String) -> Unit
) {
  val screenState by remember { mutableStateOf(SignUpScreenState()) }
  val signUpState by signUpViewModel.signUpState.observeAsState()

  when (signUpState) {
    is SignUpState.Loading -> screenState.toggleLoading()
    is SignUpState.SignedUp -> onSignedUp((signUpState as SignUpState.SignedUp).user.id)
    is SignUpState.BadEmail -> screenState.showBadEmail()
    is SignUpState.BadPassword -> screenState.showBadPassword()
    is SignUpState.DuplicateAccount -> screenState.toggleInfoMessage(R.string.duplicateAccountError)
    is SignUpState.BackendError -> screenState.toggleInfoMessage(R.string.createAccountError)
    is SignUpState.Offline -> screenState.toggleInfoMessage(R.string.offlineError)
  }

  Box(modifier = Modifier.fillMaxSize()) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
      ScreenTitle(R.string.createAnAccount)
      Spacer(modifier = Modifier.height(16.dp))
      EmailField(
        value = screenState.email,
        isError = screenState.showBadEmail,
        onValueChange = { screenState.email = it }
      )
      PasswordField(
        value = screenState.password,
        isError = screenState.showBadPassword,
        onValueChange = { screenState.password = it }
      )
      AboutField(
        value = screenState.about,
        onValueChange = { screenState.about = it }
      )
      Spacer(modifier = Modifier.height(8.dp))
      Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
          screenState.resetUiState()
          with(screenState) {
            signUpViewModel.createAccount(email, password, about)
          }
        }
      ) {
        Text(text = stringResource(id = R.string.signUp))
      }
    }
    InfoMessage(stringResource = screenState.currentInfoMessage)
    BlockingLoading(screenState.isLoading)
  }
}

@Composable
private fun EmailField(
  value: String,
  isError: Boolean,
  onValueChange: (String) -> Unit
) {
  OutlinedTextField(
    modifier = Modifier
      .fillMaxWidth()
      .testTag(stringResource(id = R.string.email)),
    value = value,
    isError = isError,
    label = {
      val resource = if (isError) R.string.badEmailError else R.string.email
      Text(text = stringResource(id = resource))
    },
    onValueChange = onValueChange
  )
}

@Composable
private fun PasswordField(
  value: String,
  isError: Boolean,
  onValueChange: (String) -> Unit
) {
  var isVisible by remember { mutableStateOf(false) }
  val visualTransformation = if (isVisible) {
    VisualTransformation.None
  } else {
    PasswordVisualTransformation()
  }
  OutlinedTextField(
    modifier = Modifier
      .fillMaxWidth()
      .testTag(stringResource(id = R.string.password)),
    value = value,
    isError = isError,
    trailingIcon = {
      VisibilityToggle(isVisible) {
        isVisible = !isVisible
      }
    },
    visualTransformation = visualTransformation,
    label = {
      val resource = if (isError) R.string.badPasswordError else R.string.password
      Text(text = stringResource(id = resource))
    },
    onValueChange = onValueChange
  )
}

@Composable
private fun VisibilityToggle(
  isVisible: Boolean,
  onToggle: () -> Unit
) {
  IconButton(onClick = {
    onToggle()
  }) {
    val resource = if (isVisible) R.drawable.ic_invisible else R.drawable.ic_visible
    Icon(
      painter = painterResource(id = resource),
      contentDescription = stringResource(id = R.string.toggleVisibility)
    )
  }
}

@Composable
fun AboutField(
  value: String,
  onValueChange: (String) -> Unit
) {
  OutlinedTextField(
    modifier = Modifier.fillMaxWidth(),
    value = value,
    label = {
      Text(text = stringResource(id = R.string.about))
    },
    onValueChange = onValueChange
  )
}
