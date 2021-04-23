package nl.jovmit.friends.signup

import androidx.annotation.StringRes
import androidx.compose.foundation.background
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
import nl.jovmit.friends.signup.state.SignUpState
import nl.jovmit.friends.ui.theme.typography

@Composable
fun SignUpScreen(
  signUpViewModel: SignUpViewModel,
  onSignedUp: () -> Unit
) {

  var email by remember { mutableStateOf("") }
  var isBadEmail by remember { mutableStateOf(false) }
  var password by remember { mutableStateOf("") }
  var isBadPassword by remember { mutableStateOf(false) }
  var about by remember { mutableStateOf("") }
  val signUpState by signUpViewModel.signUpState.observeAsState()

  if (signUpState is SignUpState.SignedUp) {
    onSignedUp()
  } else if (signUpState is SignUpState.BadEmail) {
    isBadEmail = true
  } else if (signUpState is SignUpState.BadPassword) {
    isBadPassword = true
  } else if (signUpState is SignUpState.DuplicateAccount) {
    InfoMessage(R.string.duplicateAccountError)
  } else if (signUpState is SignUpState.BackendError) {
    InfoMessage(stringResource = R.string.createAccountError)
  } else if (signUpState is SignUpState.Offline) {
    InfoMessage(stringResource = R.string.offlineError)
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
        value = email,
        isError = isBadEmail,
        onValueChange = { email = it }
      )
      PasswordField(
        value = password,
        isError = isBadPassword,
        onValueChange = { password = it }
      )
      AboutField(
        value = about,
        onValueChange = { about = it }
      )
      Spacer(modifier = Modifier.height(8.dp))
      Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
          signUpViewModel.createAccount(email, password, about)
        }
      ) {
        Text(text = stringResource(id = R.string.signUp))
      }
    }
  }
}

@Composable
fun InfoMessage(@StringRes stringResource: Int) {
  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .background(MaterialTheme.colors.secondaryVariant)
  ) {
    Text(text = stringResource(id = stringResource))
  }
}

@Composable
private fun ScreenTitle(@StringRes resource: Int) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.Center
  ) {
    Text(
      text = stringResource(resource),
      style = typography.h4
    )
  }
}

@Composable
private fun EmailField(
  value: String,
  isError: Boolean,
  onValueChange: (String) -> Unit
) {
  OutlinedTextField(
    modifier = Modifier.fillMaxWidth(),
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
