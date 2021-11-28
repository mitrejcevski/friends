package nl.jovmit.friends.domain.user

class InMemoryUserDataStore(
  private var loggedInUserId: String = ""
) {

  fun loggedInUserId() = loggedInUserId
  fun storeLoggedInUserId(userId: String) {
    loggedInUserId = userId
  }
}