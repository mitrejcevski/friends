package nl.jovmit.friends.signup

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.domain.validation.RegexCredentialsValidator
import nl.jovmit.friends.signup.state.SignUpState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@ExtendWith(InstantTaskExecutorExtension::class)
class CredentialsValidationTest {

  @ParameterizedTest
  @CsvSource(
    "'email'",
    "'a@b.c'",
    "'ab@b.c'",
    "'ab@bc.c'",
    "''",
    "'     '",
  )
  fun invalidEmail(email: String) {
    val viewModel = SignUpViewModel(RegexCredentialsValidator())

    viewModel.createAccount(email, ":password:", ":about:")

    assertEquals(SignUpState.BadEmail, viewModel.signUpState.value)
  }

  @ParameterizedTest
  @CsvSource(
    "''",
    "'           '",
    "'12345678'",
    "'abcd5678'",
    "'abcDEF78'",
    "'abcdef78#$'",
    "'ABCDEF78#$'",
  )
  fun invalidPassword(password: String) {
    val viewModel = SignUpViewModel(RegexCredentialsValidator())

    viewModel.createAccount("anna@friends.com", password, ":about:")

    assertEquals(SignUpState.BadPassword, viewModel.signUpState.value)
  }
}