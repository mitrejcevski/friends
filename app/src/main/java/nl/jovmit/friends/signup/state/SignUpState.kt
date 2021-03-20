package nl.jovmit.friends.signup.state

sealed class SignUpState {

  object BadEmail : SignUpState()

  object BadPassword : SignUpState()
}
