package nl.jovmit.friends.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jovmit.friends.app.CoroutineDispatchers
import nl.jovmit.friends.domain.friends.FriendsRepository
import nl.jovmit.friends.friends.state.FriendsState

class FriendsViewModel(
  private val friendsRepository: FriendsRepository,
  private val dispatchers: CoroutineDispatchers
) : ViewModel() {

  private val mutableFriendsState = MutableLiveData<FriendsState>()
  val friendsState: LiveData<FriendsState> = mutableFriendsState

  fun loadFriends(userId: String) {
    viewModelScope.launch {
      mutableFriendsState.value = FriendsState.Loading
      mutableFriendsState.value = withContext(dispatchers.background) {
        friendsRepository.loadFriendsFor(userId)
      }
    }
  }
}