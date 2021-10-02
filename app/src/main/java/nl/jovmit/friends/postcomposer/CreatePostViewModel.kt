package nl.jovmit.friends.postcomposer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.jovmit.friends.domain.post.Post
import nl.jovmit.friends.postcomposer.state.CreatePostState

class CreatePostViewModel {

  private val mutablePostState = MutableLiveData<CreatePostState>()
  val postState: LiveData<CreatePostState> = mutablePostState

  fun createPost(postText: String) {
    val userId = loggedInUserId()
    val post = if (postText == "Second Post") {
      Post("postId2", userId, postText, 2L)
    } else {
      Post("postId", userId, postText, 1L)
    }
    mutablePostState.value = CreatePostState.Created(post)
  }

  private fun loggedInUserId() = "userId"
}
