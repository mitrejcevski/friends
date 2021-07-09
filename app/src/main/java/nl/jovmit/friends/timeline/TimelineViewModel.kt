package nl.jovmit.friends.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.jovmit.friends.domain.timeline.TimelineRepository
import nl.jovmit.friends.timeline.state.TimelineState

class TimelineViewModel(
  private val timelineRepository: TimelineRepository
) : ViewModel() {

  private val mutableTimelineState = MutableLiveData<TimelineState>()
  val timelineState: LiveData<TimelineState> = mutableTimelineState

  fun timelineFor(userId: String) {
    viewModelScope.launch {
      mutableTimelineState.value = TimelineState.Loading
      val result = timelineRepository.getTimelineFor(userId)
      mutableTimelineState.value = result
    }
  }
}