package nl.jovmit.friends.domain.validation

import java.util.regex.Pattern

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
    } else {
      CredentialsValidationResult.Valid
    }
    return result
  }
}