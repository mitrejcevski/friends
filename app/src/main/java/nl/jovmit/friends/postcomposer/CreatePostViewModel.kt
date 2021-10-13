package nl.jovmit.friends.postcomposer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.jovmit.friends.domain.post.PostRepository
import nl.jovmit.friends.postcomposer.state.CreatePostState

class CreatePostViewModel(
  private val postRepository: PostRepository
) {

  private val mutablePostState = MutableLiveData<CreatePostState>()
  val postState: LiveData<CreatePostState> = mutablePostState

  fun createPost(postText: String) {
    val result = postRepository.createNewPost(postText)
    mutablePostState.value = result
  }
}
