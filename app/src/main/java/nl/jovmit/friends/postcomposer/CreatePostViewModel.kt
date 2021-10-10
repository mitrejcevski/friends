package nl.jovmit.friends.postcomposer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.jovmit.friends.domain.post.Post
import nl.jovmit.friends.domain.user.InMemoryUserData
import nl.jovmit.friends.infrastructure.ControllableClock
import nl.jovmit.friends.postcomposer.state.CreatePostState

class CreatePostViewModel(
  private val userData: InMemoryUserData,
  val clock: ControllableClock
) {

  private val mutablePostState = MutableLiveData<CreatePostState>()
  val postState: LiveData<CreatePostState> = mutablePostState

  fun createPost(postText: String) {
    val userId = userData.loggedInUserId()
    val timestamp = clock.now()
    val post = if (postText == "Second Post") {
      Post("postId2", userId, postText, timestamp)
    } else {
      Post("postId", userId, postText, timestamp)
    }
    mutablePostState.value = CreatePostState.Created(post)
  }

}
