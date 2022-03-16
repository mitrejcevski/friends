package nl.jovmit.friends.signup

import androidx.lifecycle.SavedStateHandle
import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.app.TestDispatchers
import nl.jovmit.friends.domain.user.InMemoryUserCatalog
import nl.jovmit.friends.domain.user.InMemoryUserDataStore
import nl.jovmit.friends.domain.user.UserRepository
import nl.jovmit.friends.domain.validation.CredentialsValidationResult
import nl.jovmit.friends.domain.validation.RegexCredentialsValidator
import nl.jovmit.friends.signup.state.SignUpScreenState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
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
    val viewModel = SignUpViewModel(
      RegexCredentialsValidator(),
      UserRepository(InMemoryUserCatalog(), InMemoryUserDataStore()),
      SavedStateHandle(),
      TestDispatchers()
    )

    viewModel.createAccount(email, ":password:", ":about:")

    assertEquals(SignUpScreenState(isBadEmail = true), viewModel.screenState.value)
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
    val viewModel = SignUpViewModel(
      RegexCredentialsValidator(),
      UserRepository(InMemoryUserCatalog(), InMemoryUserDataStore()),
      SavedStateHandle(),
      TestDispatchers()
    )

    viewModel.createAccount("anna@friends.com", password, ":about:")

    assertEquals(SignUpScreenState(isBadPassword = true), viewModel.screenState.value)
  }

  @Test
  fun validCredentials() {
    val validator = RegexCredentialsValidator()

    val result = validator.validate("john@friends.com", "12ABcd3!^")

    assertEquals(CredentialsValidationResult.Valid, result)
  }
}