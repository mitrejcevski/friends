package nl.jovmit.friends.domain.post

import nl.jovmit.friends.domain.exceptions.BackendException

class UnavailablePostCatalog : PostCatalog {

  override fun addPost(userId: String, postText: String): Post {
    throw BackendException()
  }

  override suspend fun postsFor(userIds: List<String>): List<Post> {
    throw BackendException()
  }
}