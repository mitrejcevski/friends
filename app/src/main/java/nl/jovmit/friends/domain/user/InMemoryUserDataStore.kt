package nl.jovmit.friends.domain.user

class InMemoryUserDataStore(
  private var loggedInUserId: String = ""
) : UserDataStore {

  override fun loggedInUserId() = loggedInUserId

  override fun storeLoggedInUserId(userId: String) {
    loggedInUserId = userId
  }
}