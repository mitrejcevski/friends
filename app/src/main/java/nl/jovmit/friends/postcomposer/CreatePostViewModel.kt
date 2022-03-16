package nl.jovmit.friends.postcomposer

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jovmit.friends.app.CoroutineDispatchers
import nl.jovmit.friends.domain.post.Post
import nl.jovmit.friends.domain.post.PostRepository
import nl.jovmit.friends.postcomposer.state.CreateNewPostScreenState
import nl.jovmit.friends.postcomposer.state.CreatePostState

class CreatePostViewModel(
  private val postRepository: PostRepository,
  private val dispatchers: CoroutineDispatchers
) : ViewModel() {

  private val mutablePostState = MutableLiveData<CreatePostState>()
  val postState: LiveData<CreatePostState> = mutablePostState

  private val savedStateHandle = SavedStateHandle()
  val postScreenState: LiveData<CreateNewPostScreenState> =
    savedStateHandle.getLiveData(SCREEN_STATE_KEY)

  fun createPost(postText: String) {
    viewModelScope.launch {
      mutablePostState.value = CreatePostState.Loading
      val result = withContext(dispatchers.background) {
        postRepository.createNewPost(postText)
      }
      mutablePostState.value = result
      updateScreenStateFor(result)
    }
  }

  private fun updateScreenStateFor(createPostState: CreatePostState) {
    when (createPostState) {
      is CreatePostState.Created -> setPostCreated(createPostState.post)
    }
  }

  private fun setPostCreated(post: Post) {
    val currentState = currentScreenState()
    updateScreenState(currentState.copy(createdPostId = post.id))
  }

  private fun currentScreenState(): CreateNewPostScreenState {
    return savedStateHandle[SCREEN_STATE_KEY] ?: CreateNewPostScreenState()
  }

  private fun updateScreenState(newScreenState: CreateNewPostScreenState) {
    savedStateHandle[SCREEN_STATE_KEY] = newScreenState
  }

  private companion object {
    private const val SCREEN_STATE_KEY = "createPostScreenState"
  }
}
