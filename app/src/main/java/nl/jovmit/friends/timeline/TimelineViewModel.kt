package nl.jovmit.friends.timeline

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jovmit.friends.app.CoroutineDispatchers
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
      mutableTimelineState.value = withContext(dispatchers.background) {
        timelineRepository.getTimelineFor(userId)
      }
    }
  }

  private companion object {
    private const val SCREEN_STATE_KEY = "timelineScreenState"
  }
}