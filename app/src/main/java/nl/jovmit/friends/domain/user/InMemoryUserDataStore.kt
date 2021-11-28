package nl.jovmit.friends.domain.user

class InMemoryUserDataStore(
  private val loggedInUserId: String
) {

  fun loggedInUserId() = loggedInUserId
}