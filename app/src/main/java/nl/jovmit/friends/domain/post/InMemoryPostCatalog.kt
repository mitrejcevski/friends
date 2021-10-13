package nl.jovmit.friends.domain.post

class InMemoryPostCatalog(
  private val availablePosts: List<Post> = emptyList()
) : PostCatalog {

  override fun addPost(userId: String, postText: String): Post {
    TODO("Not yet implemented")
  }

  override suspend fun postsFor(userIds: List<String>): List<Post> {
    return availablePosts.filter { userIds.contains(it.userId) }
  }
}