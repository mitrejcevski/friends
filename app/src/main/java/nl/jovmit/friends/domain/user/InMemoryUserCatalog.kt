package nl.jovmit.friends.domain.user

import nl.jovmit.friends.domain.exceptions.DuplicateAccountException

class InMemoryUserCatalog(
  private val usersForPassword: MutableMap<String, MutableList<User>> = mutableMapOf()
) {

  fun createUser(
    email: String,
    password: String,
    about: String
  ): User {
    checkAccountExists(email)
    val userId = createUserIdFor(email)
    val user = User(userId, email, about)
    saveUser(password, user)
    return user
  }

  private fun checkAccountExists(email: String) {
    if (usersForPassword.values.flatten().any { it.email == email }) {
      throw DuplicateAccountException()
    }
  }

  private fun createUserIdFor(email: String): String {
    return email.takeWhile { it != '@' } + "Id"
  }

  private fun saveUser(password: String, user: User) {
    usersForPassword.getOrPut(password, ::mutableListOf).add(user)
  }
}