package nl.jovmit.friends.domain.post

import nl.jovmit.friends.domain.exceptions.BackendException
import nl.jovmit.friends.domain.exceptions.ConnectionUnavailableException
import nl.jovmit.friends.domain.user.InMemoryUserData
import nl.jovmit.friends.postcomposer.state.CreatePostState

class PostRepository(
  private val userData: InMemoryUserData,
  private val postCatalog: PostCatalog
) {

  suspend fun createNewPost(postText: String): CreatePostState {
    return try {
      val post = postCatalog.addPost(userData.loggedInUserId(), postText)
      CreatePostState.Created(post)
    } catch (backendException: BackendException) {
      CreatePostState.BackendError
    } catch (offlineException: ConnectionUnavailableException) {
      CreatePostState.Offline
    }
  }
}