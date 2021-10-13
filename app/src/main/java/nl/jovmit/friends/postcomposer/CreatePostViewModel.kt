package nl.jovmit.friends.postcomposer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.jovmit.friends.domain.post.PostRepository
import nl.jovmit.friends.postcomposer.state.CreatePostState

class CreatePostViewModel(
  private val postRepository: PostRepository
) : ViewModel() {

  private val mutablePostState = MutableLiveData<CreatePostState>()
  val postState: LiveData<CreatePostState> = mutablePostState

  fun createPost(postText: String) {
    viewModelScope.launch {
      val result = postRepository.createNewPost(postText)
      mutablePostState.value = result
    }
  }
}
