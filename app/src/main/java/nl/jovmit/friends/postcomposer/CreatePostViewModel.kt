package nl.jovmit.friends.postcomposer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.jovmit.friends.domain.post.Post
import nl.jovmit.friends.domain.user.InMemoryUserData
import nl.jovmit.friends.infrastructure.Clock
import nl.jovmit.friends.infrastructure.IdGenerator
import nl.jovmit.friends.postcomposer.state.CreatePostState

class CreatePostViewModel(
  private val userData: InMemoryUserData,
  private val clock: Clock,
  private val idGenerator: IdGenerator
) {

  private val mutablePostState = MutableLiveData<CreatePostState>()
  val postState: LiveData<CreatePostState> = mutablePostState

  fun createPost(postText: String) {
    if (postText == ":backend:") {
      mutablePostState.value = CreatePostState.BackendError
    } else if (postText == ":offline:") {
      mutablePostState.value = CreatePostState.Offline
    } else {
      val post = createNewPost(postText)
      mutablePostState.value = CreatePostState.Created(post)
    }
  }

  private fun createNewPost(postText: String): Post {
    val userId = userData.loggedInUserId()
    val timestamp = clock.now()
    val postId = idGenerator.next()
    return Post(postId, userId, postText, timestamp)
  }
}
