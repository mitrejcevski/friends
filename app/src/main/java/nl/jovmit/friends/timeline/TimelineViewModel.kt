package nl.jovmit.friends.timeline

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jovmit.friends.app.CoroutineDispatchers
import nl.jovmit.friends.domain.post.Post
import nl.jovmit.friends.domain.timeline.TimelineRepository
import nl.jovmit.friends.timeline.state.TimelineScreenState
import nl.jovmit.friends.timeline.state.TimelineState

class TimelineViewModel(
  private val timelineRepository: TimelineRepository,
  private val dispatchers: CoroutineDispatchers
) : ViewModel() {

  private val mutableTimelineState = MutableLiveData<TimelineState>()
  val timelineState: LiveData<TimelineState> = mutableTimelineState

  private val savedStateHandle = SavedStateHandle()
  val timelineScreenState: LiveData<TimelineScreenState> =
    savedStateHandle.getLiveData(SCREEN_STATE_KEY)

  fun timelineFor(userId: String) {
    viewModelScope.launch {
      mutableTimelineState.value = TimelineState.Loading
      val result = withContext(dispatchers.background) {
        timelineRepository.getTimelineFor(userId)
      }
      mutableTimelineState.value = result
      updateScreenStateFor(result)
    }
  }

  private fun updateScreenStateFor(timelineState: TimelineState) {
    when (timelineState) {
      is TimelineState.Posts -> setPosts(timelineState.posts)
    }
  }

  private fun setPosts(posts: List<Post>) {
    val screenState = currentScreenState()
    updateScreenState(screenState.copy(posts = posts))
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