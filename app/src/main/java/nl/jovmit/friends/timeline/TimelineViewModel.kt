package nl.jovmit.friends.timeline

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
import nl.jovmit.friends.domain.timeline.TimelineRepository
import nl.jovmit.friends.timeline.state.TimelineScreenState
import nl.jovmit.friends.timeline.state.TimelineState

class TimelineViewModel(
  private val timelineRepository: TimelineRepository,
  private val savedStateHandle: SavedStateHandle,
  private val dispatchers: CoroutineDispatchers
) : ViewModel() {

  val screenState: LiveData<TimelineScreenState> =
    savedStateHandle.getLiveData(SCREEN_STATE_KEY)

  fun timelineFor(userId: String) {
    viewModelScope.launch {
      setLoading()
      val result = withContext(dispatchers.background) {
        timelineRepository.getTimelineFor(userId)
      }
      updateScreenStateFor(result)
    }
  }

  private fun updateScreenStateFor(timelineState: TimelineState) {
    when (timelineState) {
      is TimelineState.Posts -> setPosts(timelineState.posts)
      is TimelineState.BackendError -> setError(R.string.fetchingTimelineError)
      is TimelineState.OfflineError -> setError(R.string.offlineError)
    }
  }

  private fun setLoading() {
    val screenState = currentScreenState()
    updateScreenState(screenState.copy(isLoading = true))
  }

  private fun setPosts(posts: List<Post>) {
    val screenState = currentScreenState()
    updateScreenState(screenState.copy(isLoading = false, posts = posts))
  }

  private fun setError(@StringRes errorResource: Int) {
    val screenState = currentScreenState()
    updateScreenState(screenState.copy(isLoading = false, error = errorResource))
  }

  private fun currentScreenState(): TimelineScreenState {
    return savedStateHandle[SCREEN_STATE_KEY] ?: TimelineScreenState()
  }

  private fun updateScreenState(newState: TimelineScreenState) {
    savedStateHandle[SCREEN_STATE_KEY] = newState
  }

  private companion object {
    private const val SCREEN_STATE_KEY = "timelineScreenState"
  }
}