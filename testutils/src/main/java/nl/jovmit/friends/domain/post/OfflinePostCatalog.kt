package nl.jovmit.friends.domain.post

import nl.jovmit.friends.domain.exceptions.ConnectionUnavailableException

class OfflinePostCatalog : PostCatalog {

  override suspend fun addPost(userId: String, postText: String): Post {
    throw ConnectionUnavailableException()
  }

  override suspend fun postsFor(userIds: List<String>): List<Post> {
    throw ConnectionUnavailableException()
  }
}