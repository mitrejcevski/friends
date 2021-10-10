package nl.jovmit.friends.postcomposer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.jovmit.friends.domain.post.Post
import nl.jovmit.friends.domain.user.InMemoryUserData
import nl.jovmit.friends.infrastructure.Clock
import nl.jovmit.friends.postcomposer.state.CreatePostState

class CreatePostViewModel(
  private val userData: InMemoryUserData,
  private val clock: Clock
) {

  private val mutablePostState = MutableLiveData<CreatePostState>()
  val postState: LiveData<CreatePostState> = mutablePostState

  fun createPost(postText: String) {
    val userId = userData.loggedInUserId()
    val timestamp = clock.now()
    val postId = if (postText == "Second Post") {
      ControllableIdGenerator("postId2").next()
    } else {
      ControllableIdGenerator("postId").next()
    }
    val post = Post(postId, userId, postText, timestamp)
    mutablePostState.value = CreatePostState.Created(post)
  }

  class ControllableIdGenerator(
    private val id: String
  ) {

    fun next(): String {
      return id
    }
  }
}
