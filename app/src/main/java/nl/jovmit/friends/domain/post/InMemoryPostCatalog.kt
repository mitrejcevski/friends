package nl.jovmit.friends.domain.post

class InMemoryPostCatalog(
  private val availablePosts: List<Post> = emptyList()
) : PostCatalog {

  override fun postsFor(userIds: List<String>): List<Post> {
    return availablePosts.filter { userIds.contains(it.userId) }
  }
}