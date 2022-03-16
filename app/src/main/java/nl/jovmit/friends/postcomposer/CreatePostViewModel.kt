package nl.jovmit.friends.postcomposer

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jovmit.friends.R
import nl.jovmit.friends.app.CoroutineDispatchers
import nl.jovmit.friends.domain.post.Post
import nl.jovmit.friends.domain.post.PostRepository
import nl.jovmit.friends.postcomposer.state.CreateNewPostScreenState
import nl.jovmit.friends.postcomposer.state.CreatePostState

class CreatePostViewModel(
  private val postRepository: PostRepository,
  private val dispatchers: CoroutineDispatchers
) : ViewModel() {

  private val savedStateHandle = SavedStateHandle()
  val screenState: LiveData<CreateNewPostScreenState> =
    savedStateHandle.getLiveData(SCREEN_STATE_KEY)

  fun updatePostText(postText: String) {
    val currentState = currentScreenState()
    updateScreenState(currentState.copy(postText = postText))
  }

  fun createPost(postText: String) {
    viewModelScope.launch {
      setLoading()
      val result = withContext(dispatchers.background) {
        postRepository.createNewPost(postText)
      }
      updateScreenStateFor(result)
    }
  }

  private fun updateScreenStateFor(createPostState: CreatePostState) {
    when (createPostState) {
      is CreatePostState.Created -> setPostCreated(createPostState.post)
      is CreatePostState.BackendError -> setError(R.string.creatingPostError)
      is CreatePostState.Offline -> setError(R.string.offlineError)
    }
  }

  private fun setLoading() {
    val currentState = currentScreenState()
    updateScreenState(currentState.copy(isLoading = true))
  }

  private fun setPostCreated(post: Post) {
    val currentState = currentScreenState()
    updateScreenState(currentState.copy(isLoading = false, createdPostId = post.id))
  }

  private fun setError(@StringRes errorResource: Int) {
    val currentState = currentScreenState()
    updateScreenState(currentState.copy(isLoading = false, error = errorResource))
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
