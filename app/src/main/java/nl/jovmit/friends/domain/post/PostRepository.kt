package nl.jovmit.friends.domain.post

import nl.jovmit.friends.domain.exceptions.BackendException
import nl.jovmit.friends.domain.exceptions.ConnectionUnavailableException
import nl.jovmit.friends.domain.user.InMemoryUserData
import nl.jovmit.friends.infrastructure.Clock
import nl.jovmit.friends.infrastructure.IdGenerator
import nl.jovmit.friends.postcomposer.state.CreatePostState

class PostRepository(
  private val userData: InMemoryUserData,
  private val clock: Clock,
  private val idGenerator: IdGenerator
) {

  fun createNewPost(postText: String): CreatePostState {
    return try {
      val post = addPost(userData.loggedInUserId(), postText)
      CreatePostState.Created(post)
    } catch (backendException: BackendException) {
      CreatePostState.BackendError
    } catch (offlineException: ConnectionUnavailableException) {
      CreatePostState.Offline
    }
  }

  private fun addPost(userId: String, postText: String): Post {
    if (postText == ":backend:") {
      throw BackendException()
    } else if (postText == ":offline:") {
      throw ConnectionUnavailableException()
    }
    val timestamp = clock.now()
    val postId = idGenerator.next()
    return Post(postId, userId, postText, timestamp)
  }
}