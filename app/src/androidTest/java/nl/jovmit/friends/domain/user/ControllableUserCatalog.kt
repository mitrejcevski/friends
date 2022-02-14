package nl.jovmit.friends.domain.user

class ControllableUserCatalog(
    private val userCreate: suspend (String, String, String) -> User = { email, _, about ->
      User(email.takeWhile { it == '@' } + "Id", email, about)
    },
    private val followedByLoad: suspend () -> List<String> = { emptyList() },
    private val friendsLoad: suspend () -> List<Friend> = { emptyList() }
  ) : UserCatalog {

    override suspend fun createUser(email: String, password: String, about: String): User {
      return userCreate(email, password, about)
    }

    override suspend fun followedBy(userId: String): List<String> {
      return followedByLoad()
    }

    override suspend fun loadFriendsFor(userId: String): List<Friend> {
      return friendsLoad()
    }
  }