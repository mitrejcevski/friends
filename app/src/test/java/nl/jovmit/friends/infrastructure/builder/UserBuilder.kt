package nl.jovmit.friends.infrastructure.builder

import nl.jovmit.friends.domain.user.User
import java.util.*

class UserBuilder {

  private var userId = UUID.randomUUID().toString()
  private var userEmail = "user@friends.com"
  private var aboutUser = "About User"

  companion object {
    fun aUser(): UserBuilder {
      return UserBuilder()
    }
  }

  fun withId(id: String): UserBuilder = this.apply {
    userId = id
  }

  fun build(): User {
    return User(userId, userEmail, aboutUser)
  }
}