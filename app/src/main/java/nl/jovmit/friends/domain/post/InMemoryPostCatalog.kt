package nl.jovmit.friends.domain.post

import nl.jovmit.friends.infrastructure.Clock
import nl.jovmit.friends.infrastructure.IdGenerator
import nl.jovmit.friends.infrastructure.SystemClock
import nl.jovmit.friends.infrastructure.UUIDGenerator

class InMemoryPostCatalog(
  private val availablePosts: List<Post> = emptyList(),
  private val idGenerator: IdGenerator = UUIDGenerator(),
  private val clock: Clock = SystemClock()
) : PostCatalog {

  override fun addPost(userId: String, postText: String): Post {
    val timestamp = clock.now()
    val postId = idGenerator.next()
    return Post(postId, userId, postText, timestamp)
  }

  override suspend fun postsFor(userIds: List<String>): List<Post> {
    return availablePosts.filter { userIds.contains(it.userId) }
  }
}