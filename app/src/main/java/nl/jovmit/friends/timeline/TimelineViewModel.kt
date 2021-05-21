package nl.jovmit.friends.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.jovmit.friends.timeline.state.TimelineState

class TimelineViewModel {

  private val mutableTimelineState = MutableLiveData<TimelineState>()
  val timelineState: LiveData<TimelineState> = mutableTimelineState

  fun timelineFor(userId: String) {
    mutableTimelineState.value = TimelineState.Posts(emptyList())
  }
}
