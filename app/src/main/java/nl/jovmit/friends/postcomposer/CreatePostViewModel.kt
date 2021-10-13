package nl.jovmit.friends.postcomposer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.jovmit.friends.domain.post.PostRepository
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
    val result = PostRepository(userData, clock, idGenerator)
      .createNewPost(postText)
    mutablePostState.value = result
  }
}
